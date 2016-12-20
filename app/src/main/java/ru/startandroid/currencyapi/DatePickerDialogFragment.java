package ru.startandroid.currencyapi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerDialogFragment extends DialogFragment {

    private DatePicker datePicker;

    public static DatePickerDialogFragment newInstance(Date date){
        Bundle arguments = new Bundle();
        arguments.putSerializable("date", date);
        DatePickerDialogFragment dialog = new DatePickerDialogFragment();
        dialog.setArguments(arguments);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dp_layout, null);
        datePicker = (DatePicker)view.findViewById(R.id.datePicker);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date)getArguments().getSerializable("date"));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("ZHEKA", day + month + year + "");

        datePicker.init(year, month, day, null);

        builder.setView(view);
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Date date = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth())
                        .getTime();
                getTargetFragment().onActivityResult(getTargetRequestCode(), 100, new Intent()
                                            .putExtra("date", date));
            }
        });

        return builder.create();
    }
}
