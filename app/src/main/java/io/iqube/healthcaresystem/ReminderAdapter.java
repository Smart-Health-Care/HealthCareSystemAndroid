package io.iqube.healthcaresystem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import io.iqube.healthcaresystem.models.Reminder;

/**
 * Created by Srinath on 07-03-2017.
 */

class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private List<Reminder> reminderList;
    private onLongClick onLongClick;

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView time,date;
        ListView medicineListView;
        TextView reminder;
        ViewHolder(View itemView) {
            super(itemView);
            reminder = (TextView) itemView.findViewById(R.id.reminder_number);
            time = (TextView) itemView.findViewById(R.id.time);
            date = (TextView) itemView.findViewById(R.id.date);
            medicineListView = (ListView) itemView.findViewById(R.id.medicine_list);
        }
    }

    ReminderAdapter(List<Reminder> remindersList, ReminderAdapter.onLongClick onLongClick) {
        this.reminderList = remindersList;
        this.onLongClick = onLongClick;
    }

    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.existing_reminder_layout,parent,false);
        return new ViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final ReminderAdapter.ViewHolder holder, int position) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.itemView.getContext(),R.layout.medicine_list_item_layout);
        Reminder reminder = reminderList.get(position);
        try {
            for (int i = 0; i < reminder.getMedicineList().size(); i++) {
                adapter.add(reminder.getMedicineList().get(i).getName());
            }
            holder.reminder.setText("Reminder "+(reminder.getId()+1));
        holder.medicineListView.setAdapter(adapter);
        holder.time.setText("Time: "+reminder.getTime());
        holder.date.setText("Date: "+reminder.getDate());
        }catch (Exception e){
            e.printStackTrace();
        }
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
        return reminderList.size();
    }


    interface onLongClick{
        void OnLongClick(int position);
    }
}