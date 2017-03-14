package io.iqube.healthcaresystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.iqube.healthcaresystem.models.Medicine;
import io.iqube.healthcaresystem.models.Reminder;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReminderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReminderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReminderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FloatingActionButton fab;

    private List<Reminder> reminderList;
    private ReminderAdapter reminderAdapter;
    private RecyclerView recyclerView;
    private ReminderAdapter.onLongClick onLongClick;


    private OnFragmentInteractionListener mListener;

    public ReminderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReminderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReminderFragment newInstance(String param1, String param2) {
        ReminderFragment fragment = new ReminderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder, container, false);
    }
    final String[] dates = new String[1];

    int hour,minutes, dates1, months, years;
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        dates[0] = "";
        Realm.init(getContext());
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Reminder> results = realm.where(Reminder.class).findAll();
        reminderList = new ArrayList<>();
        reminderList.clear();

        for(int i=0;i<results.size();i++){
            reminderList.add(results.get(i));
        }

        onLongClick = new ReminderAdapter.onLongClick() {
            @Override
            public void OnLongClick(int position) {

                final LinearLayout linearLayout = new LinearLayout(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                linearLayout.setLayoutParams(layoutParams);
                final View v = View.inflate(getContext(),R.layout.new_reminder_layout,linearLayout);
                final String[] time = new String[1];
                time[0] = "";
                final String[] date = new String[1];
                date[0] = "";
                final ListView medicineListView = (ListView) linearLayout.findViewById(R.id.medicine_list);
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1);
                Realm.init(getContext());
                Realm realm = Realm.getDefaultInstance();
                final RealmResults<Medicine> results1 = realm.where(Medicine.class).findAll();
                final RealmList<Medicine> selectedList = new RealmList<>();
                for(int i=0;i<results1.size();i++)
                    adapter.add(results1.get(i).getName());

                final LinearLayout select_layout = (LinearLayout) linearLayout.findViewById(R.id.select_list);
                medicineListView.setAdapter(adapter);
                medicineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView textView = new TextView(getContext());
                        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        textView.setText(adapter.getItem(position));
                        String name = adapter.getItem(position);
                        for(int i=0;i<results1.size();i++){
                            if(results1.get(i).getName().equals(name)){
                                selectedList.add(results1.get(i));
                            }
                        }
                        adapter.remove(adapter.getItem(position));
                        adapter.notifyDataSetChanged();
                        select_layout.addView(textView);
                    }
                });



                new AlertDialog.Builder(getContext())
                        .setView(v)
                        .setCancelable(true)
                        .setTitle("Enter Reminder Details")
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                Number currentId =  realm.where(Reminder.class).max("id");
                                int id;
                                try {
                                    id = currentId.intValue()+1;
                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                    id = 0;
                                }
                                Reminder reminder = new Reminder(id,time[0],date[0],selectedList);
                                realm.copyToRealm(reminder);
                                realm.commitTransaction();
                                reminderList.add(reminder);
                                reminderAdapter.notifyDataSetChanged();
                            }
                        })
                        .create()
                        .show();
                TextView timed  = (TextView) v.findViewById(R.id.time);
                timed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                time[0] = String.valueOf(hourOfDay+minute);
                            }
                        },0,0,false);
                        timePickerDialog.show();
                    }
                });

                final TextView dated = (TextView) v.findViewById(R.id.date);
                dated.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    date[0] = ""+dayOfMonth+"/"+month+"/"+year;
                                    dated.setText(date[0]);
                                }
                            });
                        } else {
                            DatePicker datePicker = new DatePicker(getContext());
                            date[0] = datePicker.getDayOfMonth()+"/"+datePicker.getMonth()+"/"+datePicker.getYear();
                        }
                    }
                });

            }
        };

        reminderAdapter = new ReminderAdapter(reminderList,onLongClick);
        reminderAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        if(reminderList.size()==0){
            Snackbar.make(recyclerView,"No Reminders Added, Click the '+' icon to add Reminders", BaseTransientBottomBar.LENGTH_LONG).show();
        }else {
            recyclerView.setAdapter(reminderAdapter);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vs) {
                final RealmList<Medicine> selectedList = new RealmList<>();
                final LinearLayout linearLayout = new LinearLayout(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                linearLayout.setLayoutParams(layoutParams);
                final View v = View.inflate(getContext(),R.layout.new_reminder_layout,linearLayout);
                final String[] time = new String[1];
                time[0] = "";

                final ListView medicineListView = (ListView) linearLayout.findViewById(R.id.medicine_list);
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1);
                Realm.init(getContext());
                Realm realm = Realm.getDefaultInstance();
                final RealmResults<Medicine> results1 = realm.where(Medicine.class).findAll();

                for(int i=0;i<results1.size();i++) {
                    adapter.add(results1.get(i).getName());
                }

                final LinearLayout select_layout = (LinearLayout) linearLayout.findViewById(R.id.select_list);
                medicineListView.setAdapter(adapter);
                medicineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView textView = new TextView(getContext());
                        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        textView.setText(adapter.getItem(position));
                        String name = adapter.getItem(position);
                        for(int i=0;i<results1.size();i++){
                            if(results1.get(i).getName().equals(name)){
                                selectedList.add(results1.get(i));
                            }
                        }
                        adapter.remove(adapter.getItem(position));
                        adapter.notifyDataSetChanged();
                        select_layout.addView(textView);
                    }
                });


                new AlertDialog.Builder(getContext())
                        .setView(v)
                        .setTitle("Enter Reminder Details")
                        .setPositiveButton("Add Reminder", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                Number currentId =  realm.where(Reminder.class).max("id");
                                int id;
                                try {
                                    id = currentId.intValue()+1;
                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                    id = 0;
                                }

                                Reminder medicine = new Reminder(id,time[0],dates[0],selectedList);
                                realm.copyToRealmOrUpdate(medicine);
                                realm.commitTransaction();
                                reminderList.add(medicine);
                                reminderAdapter.notifyDataSetChanged();
                                recyclerView.refreshDrawableState();
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(years, months,ReminderFragment.this.dates1,hour,minutes);
                                Alarm alarm = new Alarm();
                                alarm.setAlarm(getContext(),calendar.getTimeInMillis());
                            }
                        })
                        .create()
                        .show();
                final TextView timetv  = (TextView) v.findViewById(R.id.time);
                timetv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                time[0] = String.valueOf(hourOfDay+":"+minute);
                                hour = hourOfDay;
                                minutes = minute;
                                timetv.setText(time[0]);
                                view.animate();
                            }
                        },0,0,true);
                        timePickerDialog.show();
                    }
                });

                final TextView dattv = (TextView) v.findViewById(R.id.date);
                dattv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            Calendar calendar = Calendar.getInstance();
                            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    dates[0] = ""+dayOfMonth+"/"+month+"/"+year;
                                    ReminderFragment.this.dates1 = dayOfMonth;
                                    ReminderFragment.this.months = month;
                                    ReminderFragment.this.years = year;
                                    dattv.setText(dates[0]);

                                }
                            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                            datePickerDialog.show();

                    }
                });

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
