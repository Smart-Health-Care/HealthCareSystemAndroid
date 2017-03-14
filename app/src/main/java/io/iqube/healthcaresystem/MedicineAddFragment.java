package io.iqube.healthcaresystem;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.github.clans.fab.FloatingActionMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import io.iqube.healthcaresystem.models.Medicine;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MedicineAddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MedicineAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicineAddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

//Coding by Srinath
    private com.github.clans.fab.FloatingActionButton fab;

    private List<Medicine> medicineList;
    private MedicineAdapter medicineAdapter;
    private RecyclerView recyclerView;
    private MedicineAdapter.onLongClick onLongClick;
    private FloatingActionMenu menu;

    public MedicineAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MedicineAddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicineAddFragment newInstance(String param1, String param2) {
        MedicineAddFragment fragment = new MedicineAddFragment();
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
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        Realm.init(getContext());
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
        fab = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.fab1);
        menu = (FloatingActionMenu) view.findViewById(R.id.menu);
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Medicine> results = realm.where(Medicine.class).findAll();
        medicineList = new ArrayList<>();
        medicineList.clear();

        for(int i=1;i<=results.size();i++){
            medicineList.add(results.get(i-1));
        }

       onLongClick = new MedicineAdapter.onLongClick() {
           @Override
           public void OnLongClick(final int position) {
               final View v = LayoutInflater.from(getContext()).inflate(R.layout.new_medicine_entry_layout,recyclerView,false);
               new AlertDialog.Builder(getContext())
                       .setView(v)
                       .setTitle("Edit Medicine")
                       .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                               EditText nameet = (EditText) v.findViewById(R.id.medicine_name);
                               String name = nameet.getText().toString();
                               Realm realm = Realm.getDefaultInstance();
                               realm.beginTransaction();
                               RealmResults<Medicine> results1 = realm.where(Medicine.class).equalTo("name",name).findAll();
                               Medicine medicine = results.get(0);
                               medicineList.remove(medicine);
                               if(medicineList.size()==0){
                                   Snackbar.make(recyclerView,"All Medicines Removed, Click the '+' icon to add Medicines", BaseTransientBottomBar.LENGTH_LONG).show();
                               }
                               results1.deleteAllFromRealm();
                               realm.commitTransaction();
                               medicineAdapter.notifyDataSetChanged();
                           }
                       })
                       .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                               EditText nameet = (EditText) v.findViewById(R.id.medicine_name);
                               EditText doseet = (EditText) v.findViewById(R.id.dosage);
                               EditText qtyet = (EditText) v.findViewById(R.id.qty);
                               EditText preset = (EditText) v.findViewById(R.id.prescription);
                               String name = nameet.getText().toString();
                               String dosage = doseet.getText().toString();
                               String qty = qtyet.getText().toString();
                               String prescription = preset.getText().toString();
                               Realm realm = Realm.getDefaultInstance();
                               realm.beginTransaction();
                               RealmResults<Medicine> results1=  realm.where(Medicine.class).equalTo("name",name).findAll();
                               Medicine medicine = results1.get(0);
                               realm.copyToRealmOrUpdate(new Medicine(medicine.getId(),name,dosage,qty,prescription));
                               Medicine medicine1 = medicineList.get(position);
                               medicineList.remove(medicine1.getId());
                               medicine1.setName(name);
                               medicine1.setDosage(dosage);
                               medicine1.setQuantity(qty);
                               medicine1.setPrescription(prescription);
                               medicineList.add(medicine1.getId(),medicine1);
                               medicineAdapter.notifyDataSetChanged();
                               realm.commitTransaction();

                           }
                       })
                       .create()
                       .show();
               EditText nameet = (EditText) v.findViewById(R.id.medicine_name);
               EditText doseet = (EditText) v.findViewById(R.id.dosage);
               EditText qtyet = (EditText) v.findViewById(R.id.qty);
               EditText preset = (EditText) v.findViewById(R.id.prescription);
               Medicine medicine = medicineList.get(position);
               nameet.setText(medicine.getName());
               doseet.setText(medicine.getDosage());
               qtyet.setText(medicine.getQuantity());
               preset.setText(medicine.getPrescription());

           }
       };

        medicineAdapter = new MedicineAdapter(medicineList,onLongClick);
        medicineAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        if(medicineList.size()==0){
            Snackbar.make(recyclerView,"No Medicines Added, Click the '+' icon to add Medicines", BaseTransientBottomBar.LENGTH_LONG).show();
        }else {
            recyclerView.setAdapter(medicineAdapter);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vs) {

////                final View layout = View.inflate(getContext(),R.layout.new_medicine_entry_layout, linearLayout);
////                FloatingActionButton fab1 = (FloatingActionButton) layout.findViewById(R.id.done);
////                fab1.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        String name;
////                        int dosage;
////                        EditText nameet = (EditText) layout.findViewById(R.id.medicine_name);
////                        EditText dosageet = (EditText) layout.findViewById(R.id.dosage);
////                        name = nameet.getText().toString();
////                        Realm.init(getContext());
////                        Realm realm = Realm.getDefaultInstance();
////                        realm.beginTransaction();
////                        Number currentId =  realm.where(Medicine.class).max("id");
////                        int id = currentId.intValue();
////                        dosage = Integer.parseInt(dosageet.getText().toString());
////                        Medicine medicine = new Medicine(id+1,name,dosage);
////                        realm.copyToRealm(medicine);
////                        realm.commitTransaction();
////
////                    }
////                });

                LinearLayout linearLayout = new LinearLayout(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                linearLayout.setLayoutParams(layoutParams);
                final View v = View.inflate(getContext(),R.layout.new_medicine_entry_layout,linearLayout);
                new AlertDialog.Builder(getContext())
                        .setView(v)
                        .setTitle("Enter Medicine Details")
                        .setPositiveButton("Add Medicine", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                EditText nameet = (EditText) v.findViewById(R.id.medicine_name);
                                EditText doseet = (EditText) v.findViewById(R.id.dosage);
                                EditText qtyet = (EditText) v.findViewById(R.id.qty);
                                EditText preset = (EditText) v.findViewById(R.id.prescription);
                                String name = nameet.getText().toString();
                                String dosage = doseet.getText().toString();
                                String qty = qtyet.getText().toString();
                                String prescription = preset.getText().toString();
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                Number currentId =  realm.where(Medicine.class).max("id");
                                int id;
                                try {
                                    id = currentId.intValue()+1;
                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                    id = 0;
                                }
                                Medicine medicine = new Medicine(id,name,dosage,qty,prescription);
                                realm.copyToRealm(medicine);
                                realm.commitTransaction();
                                medicineList.add(medicine);
                                medicineAdapter.notifyDataSetChanged();
                                menu.close(true);
                            }
                        })
                        .create()
                        .show();


            }

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medicine_add, container, false);
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
