import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.scope.InternalArtifactType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register

class HidePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.configure(ApplicationAndroidComponentsExtension::class) {
            onVariants(selector().withBuildType("release")) { variant ->
                val deleteMappingTaskProvider = target.tasks.register<DeleteMappingTask>("deleteMappingTask${variant.buildType}")
                variant.artifacts.use(deleteMappingTaskProvider)
                    .wiredWithFiles(
                        DeleteMappingTask::inputAabFile,
                        DeleteMappingTask::outputAabFile,
                    )
                    .toTransform(InternalArtifactType.INTERMEDIARY_BUNDLE)
            }
        }
    }
}