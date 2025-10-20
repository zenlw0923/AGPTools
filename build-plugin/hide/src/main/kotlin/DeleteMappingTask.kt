import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

abstract class DeleteMappingTask : DefaultTask() {
    @get:InputFile
    abstract val inputAabFile: RegularFileProperty

    @get:OutputFile
    abstract val outputAabFile: RegularFileProperty

    @TaskAction
    fun doTaskAction() {
        val inputFile = inputAabFile.get().asFile
        val outputFile = outputAabFile.get().asFile
        val tempFile = File(outputFile.parent, "${outputFile.name}.tmp")
        ZipFile(inputFile).use { zipIn ->
            ZipOutputStream(tempFile.outputStream().buffered()).use { zipOut ->
                val entries = zipIn.entries()
                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    if (entry.name.endsWith("proguard.map")) {
                        continue
                    }
                    val newEntry = ZipEntry(entry)
                    zipOut.putNextEntry(newEntry)

                    if (!entry.isDirectory) {
                        zipIn.getInputStream(entry).use { input ->
                            input.copyTo(zipOut)
                        }
                    }

                    zipOut.closeEntry()
                }
            }
        }

        if (outputFile.exists()) {
            outputFile.delete()
        }
        tempFile.renameTo(outputFile)
    }
}