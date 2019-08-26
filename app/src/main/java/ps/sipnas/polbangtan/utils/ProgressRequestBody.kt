package ps.sipnas.polbangtan.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.MimeTypeMap
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.*


/**
 * *********************************************
 * Created by ukie on 11/4/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 * *********************************************
 * © 2018 | All Right Reserved
 */
class ProgressRequestBody(private val mFile: File, private val content_type: String, private val mListener: UploadCallbacks,
                          private val context: Context) : RequestBody() {

    override fun contentType(): MediaType? {
        return MediaType.parse("$content_type/*")
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mFile.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength = mFile.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        Log.e("test", getMimeType(context, Uri.fromFile(mFile)))
        val inputStream = try {
            FileInputStream(mFile)
        } catch (e: FileNotFoundException) {
            getInputStreamForVirtualFile(Uri.fromFile(mFile), getMimeType(context, Uri.fromFile(mFile))
                    ?: "")
        }


        var uploaded: Long = 0
        inputStream.use { input ->
            var read = 0
            val handler = Handler(Looper.getMainLooper())
            while (read != -1) {

                // update progress on UI thread
                handler.post(ProgressUpdater(uploaded, fileLength))

                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                read = input.read(buffer)
            }
        }
    }

    @Throws(IOException::class)
    private fun getInputStreamForVirtualFile(uri: Uri, mimeTypeFilter: String): InputStream {
        val resolver = context.contentResolver
        val openableMimeTypes = resolver.getStreamTypes(uri, mimeTypeFilter)

        if (openableMimeTypes == null || openableMimeTypes.isEmpty()) {
            throw FileNotFoundException()
        }

        return resolver
                .openTypedAssetFileDescriptor(uri, openableMimeTypes[0], null)!!
                .createInputStream()
    }


    private fun getMimeType(context: Context, uri: Uri): String? {
        val extension: String?

        //Check uri format to avoid null
        extension = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //If scheme is a content
            val mime = MimeTypeMap.getSingleton()
            mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
        }

        return extension
    }

    interface UploadCallbacks {
        fun onProgressUpdate(percentage: Int)

        fun onError()

        fun onFinish()
    }

    private inner class ProgressUpdater(private val mUploaded: Long, private val mTotal: Long) : Runnable {

        override fun run() {
            mListener.onProgressUpdate((100 * mUploaded / mTotal).toInt())
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }
}