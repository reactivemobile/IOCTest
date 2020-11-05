import annotations.Inject
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("annotations.Inject")
@SupportedOptions(AnnotationProcessor.KAPT_KOTLIN_GENERATED_NAME)
class AnnotationProcessor : AbstractProcessor() {

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (annotations.isEmpty()) {
            return false
        }

        val fileBuilder: FileSpec.Builder = FileSpec.builder(packageName = "processor", fileName = "Processor")

        val elementMap = mapFieldsToEnclosingClasses(roundEnv)

        elementMap.forEach { entry ->
            val classBeingInjectedParameterName = entry.key.toParameterName()
            val classBeingInjectedTypeName = entry.key.toTypeName()

            val injectFunctionBuilder = FunSpec.builder("inject")
                .addModifiers(KModifier.PUBLIC)
                .addParameter(classBeingInjectedParameterName, classBeingInjectedTypeName)

            entry.value.forEach { field ->
                val classDotField = "$classBeingInjectedParameterName.$field"
                val injectedTypeConstructor = "${field.toTypeName()}()"

                injectFunctionBuilder.addCode(CodeBlock.of("$classDotField = $injectedTypeConstructor\n"))
            }

            fileBuilder.addFunction(injectFunctionBuilder.build())
        }

        val file = fileBuilder.build()
        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_NAME]
        kaptKotlinGeneratedDir?.let { file.writeTo(File(it)) } ?: println("Error getting processing options")

        return true
    }

    private fun mapFieldsToEnclosingClasses(roundEnv: RoundEnvironment): HashMap<Element, MutableList<Element>> {
        val elementMap = HashMap<Element, MutableList<Element>>()

        roundEnv.getElementsAnnotatedWith(Inject::class.java).forEach { element ->
            element.enclosingElement.let {
                elementMap.putIfAbsent(it, mutableListOf(element))?.add(element)
            }
        }
        return elementMap
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_NAME = "kapt.kotlin.generated"
    }

    private fun Element.toParameterName() = this.simpleName.toString().toLowerCase() + "Param"
    private fun Element.toTypeName() = this.asType().asTypeName()
}