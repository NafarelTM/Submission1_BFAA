package com.dicoding.submissionfundamental1.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.dicoding.submissionfundamental1.AlarmReceiver
import com.dicoding.submissionfundamental1.database.ReminderPreference
import com.dicoding.submissionfundamental1.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    companion object{
        private const val TYPE_ALARM = "RepeatingAlarm"
    }

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var alarmReceiver: AlarmReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmReceiver = AlarmReceiver()

        val reminderPref = ReminderPreference(this)
        binding.switchReminder.isChecked = reminderPref.getReminder()

        binding.switchReminder.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                reminderPref.setReminder(true)
                alarmReceiver.setAlarm(this, TYPE_ALARM, "09:00", "Github user favorite reminder")
            } else{
                reminderPref.setReminder(false)
                alarmReceiver.cancelAlarm(this)
            }
        }

        binding.localization.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }


}