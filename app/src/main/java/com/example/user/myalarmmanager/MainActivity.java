package com.example.user.myalarmmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvOneTimeDate, tvOneTimeTime;
    private TextView tvRepeatingTime;
    private EditText edtOneTimeMessage, edtRepeatingMessage;
    private Button btnOneTimeDate, btnOneTimeTime, btnOnetime, btnRepeatingTime, btnRepeting, btnCancelRepeatingAlarm;
    private Calendar calOneTimeDate, calOneTimeTime, calRepeatTimeTime;
    private AlarmReceiver alarmReceiver;
    private AlarmPreferences alarmPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvOneTimeDate = (TextView) findViewById(R.id.tv_one_time_alarm_date);
        tvOneTimeTime = (TextView) findViewById(R.id.tv_one_time_alarm_time);
        edtOneTimeMessage = (EditText) findViewById(R.id.edt_one_time_alarm_message);
        btnOneTimeDate = (Button) findViewById(R.id.btn_one_time_alarm_date);
        btnOneTimeTime = (Button) findViewById(R.id.btn_one_time_alarm_time);
        btnOnetime = (Button) findViewById(R.id.btn_set_one_time_alarm);
        tvRepeatingTime = (TextView) findViewById(R.id.tv_repeating_time_alarm_time);
        edtRepeatingMessage = (EditText) findViewById(R.id.edt_repeating_alarm_message);
        btnRepeatingTime = (Button) findViewById(R.id.btn_repeating_time_alarm_time);
        btnRepeting = (Button) findViewById(R.id.btn_repeating_time_alarm);
        btnCancelRepeatingAlarm = (Button) findViewById(R.id.btn_cancel_repeating_alarm);

        btnOneTimeDate.setOnClickListener(this);
        btnOneTimeTime.setOnClickListener(this);
        btnOnetime.setOnClickListener(this);
        btnRepeatingTime.setOnClickListener(this);
        btnRepeting.setOnClickListener(this);
        btnCancelRepeatingAlarm.setOnClickListener(this);

        calOneTimeDate = Calendar.getInstance();
        calOneTimeTime = Calendar.getInstance();
        calRepeatTimeTime = Calendar.getInstance();

        alarmPreferences = new AlarmPreferences(this);
        alarmReceiver = new AlarmReceiver();

        if (!TextUtils.isEmpty(alarmPreferences.getOneTimeDate())) {
            setOneTimeText();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_one_time_alarm_date) {
            final Calendar currentDate = Calendar.getInstance();
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    calOneTimeDate.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    tvOneTimeDate.setText(dateFormat.format(calOneTimeDate.getTime()));
                }
            }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
        } else if (v.getId() == R.id.btn_one_time_alarm_time) {
            final Calendar currentTime = Calendar.getInstance();
            new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calOneTimeTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calOneTimeTime.set(Calendar.MINUTE, minute);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    tvOneTimeTime.setText(dateFormat.format(calOneTimeTime.getTime()));
                    //Log.v(TAG, "the choosen one " + date.getTime());
                }
            }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), true).show();
        }
            else if (v.getId() == R.id.btn_repeating_time_alarm_time){
                final Calendar currentDate = Calendar.getInstance();
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calRepeatTimeTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calRepeatTimeTime.set(Calendar.MINUTE, minute);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

                        tvRepeatingTime.setText(dateFormat.format(calRepeatTimeTime.getTime()));
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();


        } else if (v.getId() == R.id.btn_set_one_time_alarm) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String oneTimeDate = dateFormat.format(calOneTimeDate.getTime());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String oneTimeTime = timeFormat.format(calOneTimeTime.getTime());
            String oneTimeMessage = edtOneTimeMessage.getText().toString();

            alarmPreferences.setOneTimeDate(oneTimeDate);
            alarmPreferences.setOneTimeTime(oneTimeTime);
            alarmPreferences.setOneTimeMesssage(oneTimeMessage);

            alarmReceiver.setOneTimeAlarm(this, AlarmReceiver.TYPE_ONE_TIME,
                    alarmPreferences.getOneTimeDate(),
                    alarmPreferences.getOneTimeTime(),
                    alarmPreferences.getOneTimeMessage());
        }

        else if (v.getId() == R.id.btn_repeating_time_alarm){
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String repeatTimeTime =  timeFormat.format(calRepeatTimeTime.getTime());
            String repeatTimeMessage = edtRepeatingMessage.getText().toString();

            alarmPreferences.setRepeatingTime(repeatTimeTime);
            alarmPreferences.setRepeatingMessage(repeatTimeMessage);

            setRepeatingText();
            alarmReceiver.setRepeatingalarm(this, AlarmReceiver.TYPE_REPEATING,
                    alarmPreferences.getRepeatingTime(), alarmPreferences.getRepeatingMessage());
        }

        else  if (v.getId() == R.id.btn_cancel_repeating_alarm){
            alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING);
        }
        if (!TextUtils.isEmpty(alarmPreferences.getRepeatingTime())){
            setRepeatingText();
        }
    }

    private void setRepeatingText(){
        tvRepeatingTime.setText(alarmPreferences.getRepeatingTime());
        edtRepeatingMessage.setText(alarmPreferences.getRepeatingMessage());
    }


    private void setOneTimeText() {
        tvOneTimeDate.setText(alarmPreferences.getOneTimeDate());
        edtOneTimeMessage.setText(alarmPreferences.getOneTimeMessage());
    }


}