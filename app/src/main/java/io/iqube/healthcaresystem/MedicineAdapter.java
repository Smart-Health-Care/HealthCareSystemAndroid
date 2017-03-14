package io.iqube.healthcaresystem;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.iqube.healthcaresystem.models.Medicine;

/**
 * Created by Srinath on 07-03-2017.
 */

class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    private List<Medicine> medicineList;
    private onLongClick onLongClick;
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView medicineName,dosage,qty,prescription;
        ViewHolder(View itemView) {
            super(itemView);
            prescription = (TextView) itemView.findViewById(R.id.prescription);
            medicineName = (TextView) itemView.findViewById(R.id.medicine_name);
            dosage = (TextView) itemView.findViewById(R.id.dosage);
            qty = (TextView) itemView.findViewById(R.id.qty);
        }
    }

    public MedicineAdapter(List<Medicine> medicineList, MedicineAdapter.onLongClick onLongClick) {
        this.medicineList = medicineList;
        this.onLongClick = onLongClick;
    }

    @Override
    public MedicineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.existing_medicine_layout,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MedicineAdapter.ViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.medicineName.setText(medicine.getName());
        holder.dosage.setText(medicine.getDosage()+" mg");
        holder.qty.setText("Qty: "+medicine.getQuantity());
        holder.prescription.setText(medicine.getPrescription());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClick.OnLongClick(holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }
    public interface onLongClick{
        void OnLongClick(int position);
    }
}
