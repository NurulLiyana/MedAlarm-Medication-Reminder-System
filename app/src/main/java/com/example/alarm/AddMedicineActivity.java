package com.example.alarm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import static android.R.attr.id;

public class AddMedicineActivity extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    Button savebutton;
    EditText med_name, med_desc, med_dose;
    EditText message;

    TimePicker myTimePicker;
    TimePickerDialog timePickerDialog;
    final Context context = this;
    final static int RQS_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        savebutton = (Button) findViewById(R.id.save_button);
        med_name = findViewById(R.id.medicine);
        med_desc = findViewById(R.id.desc);
        med_dose = findViewById(R.id.dose);

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(context);

                openTimePickerDialog(false);
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("Medicine");

                //Get Value
                String name = med_name.getText().toString();
                String description = med_desc.getText().toString();
                String dose = med_dose.getText().toString();

                MedicineLibrary medlib = new MedicineLibrary(name, description, dose);
                reference.child(name).setValue(medlib);

                Intent intent = new Intent(AddMedicineActivity.this, Home.class);
                startActivity(intent);
            }
        });
    }

    //create method to open time picker
    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(
                AddMedicineActivity.this,
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                is24r);
        timePickerDialog.setTitle("Set Time");
        timePickerDialog.show();
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener
            = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if(calSet.compareTo(calNow) <= 0){
                //Today Set time passed, count to tomorrow
                calSet.add(Calendar.DATE, 1);
            }
            setAlarm(calSet);
        }};

    private void setAlarm(Calendar targetCal) {

        message = (EditText) findViewById(R.id.medicine);

        //Passing custom value to AlarmNotificationService using pending Intent
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra("msg", message.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);

    }

}








