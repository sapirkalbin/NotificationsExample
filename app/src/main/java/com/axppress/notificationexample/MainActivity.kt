package com.axppress.notificationexample


import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.*
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.navigation.ui.AppBarConfiguration
import com.axppress.notificationexample.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private fun createNotificationChannels() {

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                CHANNEL_1_ID,
                "Channel 1",
                NotificationManager.IMPORTANCE_LOW
            )
            channel1.description = "This is Channel 1"
            val channel2 = NotificationChannel(
                CHANNEL_2_ID,
                "Channel 2",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel2.description = "This is Channel 2"
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(channel1)
            manager.createNotificationChannel(channel2)
        }
    }

    companion object {
        var MESSAGES: MutableList<Message> = ArrayList()
        const val CHANNEL_1_ID = "channel1"
        const val CHANNEL_2_ID = "channel2"


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannels()

/*
        MESSAGES.add(Message("Good morning!", "Jim"))
        MESSAGES.add(Message("Hello", "Mami"))
        MESSAGES.add(Message("Hi!", "Jenny"))

*/

        binding.button2.setOnClickListener {
            sendOnChannel2()
        }
    }


    fun sendOnChannel2() {
        val title1 = "Help I need somebodyyyyy"
        val message1 = "*help*"
        val title2 = "Not just anybodyyyy"
        val message2 = "Help!!"
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.checkbox_on_background)
        val finalPicture = getCircleBitmap(bitmap)
        val personBundle = Bundle()

        val notification1: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_delete)
                .setContentTitle(title1)
                .setContentText(message1)
                .setGroup("example_group")
                .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
                .setAutoCancel(true)
                .setLargeIcon(finalPicture)

        // this is also existing in myProject
/*        .setDeleteIntent(deleteIntent)
        .addPerson(Person.fromBundle(Bundle()))
        .setContentIntent(contentIntent)
        .setWhen(notification.time!!.timeInMillis)*/


        val notification2: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_delete)
                .setContentTitle(title2)
                .setContentText(message2)
                .setGroup("example_group")
                .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
                .setAutoCancel(true)
                .setLargeIcon(finalPicture)

        val summaryNotification: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setContentTitle("ExampleApp")
                .setLights(Color.RED, 1000, 300)
                .setNumber(5)
                .setSmallIcon(R.drawable.ic_delete)
                .setStyle(
                    NotificationCompat.InboxStyle()
                        .setSummaryText("$1 messages from $1 chats")
                ).setColor(resources.getColor(R.color.background_dark))
                .setGroup("example_group")
                .setGroupSummary(true)


        CoroutineScope(Dispatchers.Main).launch {

            delay(2000)
            notificationManager.notify(2, notification1.build())
            delay(4000)
            notificationManager.notify(3, notification2.build())
            delay(2000)
            notificationManager.notify(4, summaryNotification.build())
        }


    }
}

fun getCircleBitmap(bitmap: Bitmap): Bitmap? {
    val output: Bitmap
    val srcRect: Rect
    val dstRect: Rect
    val r: Float
    val width = bitmap.width
    val height = bitmap.height
    if (width > height) {
        output = Bitmap.createBitmap(height, height, Bitmap.Config.ARGB_8888)
        val left = (width - height) / 2
        val right = left + height
        srcRect = Rect(left, 0, right, height)
        dstRect = Rect(0, 0, height, height)
        r = (height / 2).toFloat()
    } else {
        output = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
        val top = (height - width) / 2
        val bottom = top + width
        srcRect = Rect(0, top, width, bottom)
        dstRect = Rect(0, 0, width, width)
        r = (width / 2).toFloat()
    }
    val canvas = Canvas(output)
    val color = -0xbdbdbe
    val paint = Paint()
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = color
    canvas.drawCircle(r, r, r, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, srcRect, dstRect, paint)
    bitmap.recycle()
    return output
}

class Message(val text: CharSequence, val sender: CharSequence) {
    val timestamp: Long = System.currentTimeMillis()

}