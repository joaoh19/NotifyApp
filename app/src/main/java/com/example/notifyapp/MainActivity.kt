package com.example.notifyapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.notifyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationManager: NotificationManager

    private val canalID = "1"
    private val notificacaoID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        solicitarPermissao()
        criarCanalNotificacao()

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(applicationContext, ImagemActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        binding.btnCriarNotificacao.setOnClickListener {
            emitirNotificacao(pendingIntent)
        }
    }

    private fun solicitarPermissao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }

    private fun criarCanalNotificacao() {
        val nome = "Notificações Gerais"
        val desc = "Categoria do app"
        val importancia = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(canalID, nome, importancia)
        channel.description = desc

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun emitirNotificacao(pendingIntent: PendingIntent) {

        val builder = NotificationCompat.Builder(this, canalID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(binding.edtTituloNotificacao.text.toString())
            .setContentText(binding.edtCorpoNotificacao.text.toString())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(notificacaoID, builder.build())
    }
}