package com.manilalinkup.app;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private Context context;
    private List<Job> jobList;

    private int selectedPosition = -1;

    // store saved jobs
    private Set<Integer> savedJobs = new HashSet<>();

    public JobAdapter(Context context, List<Job> jobList) {
        this.context = context;
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.job_item, parent, false);

        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {

        Job job = jobList.get(position);

        holder.jobTitle.setText(job.getTitle());
        holder.companyName.setText(job.getCompany());
        holder.jobLocation.setText(job.getLocation());
        holder.jobSalary.setText(job.getSalary());
        holder.jobSchedule.setText(job.getSchedule());

        // highlight selected card
        holder.itemView.setSelected(selectedPosition == position);

        if (selectedPosition == position) {
            holder.cardView.setCardElevation(14f);
        } else {
            holder.cardView.setCardElevation(6f);
        }

        // -------------------------
        // BOOKMARK TOGGLE
        // -------------------------
        if (savedJobs.contains(position)) {
            holder.saveJob.setImageResource(R.drawable.bookmark_filled);
        } else {
            holder.saveJob.setImageResource(R.drawable.bookmark_outline);
        }

        holder.saveJob.setOnClickListener(v -> {

            if (savedJobs.contains(position)) {
                savedJobs.remove(position);
                holder.saveJob.setImageResource(R.drawable.bookmark_outline);
            } else {
                savedJobs.add(position);
                holder.saveJob.setImageResource(R.drawable.bookmark_filled);
            }

        });

        // -------------------------
        // DELETE WITH X BUTTON
        // -------------------------
        holder.removeJob.setOnClickListener(v -> {

            int pos = holder.getAdapterPosition();

            if (pos != RecyclerView.NO_POSITION) {

                new AlertDialog.Builder(context)
                        .setTitle("Remove Job")
                        .setMessage("Are you sure you want to remove this job?")
                        .setPositiveButton("Remove", (dialog, which) -> {

                            jobList.remove(pos);
                            notifyItemRemoved(pos);
                            notifyItemRangeChanged(pos, jobList.size());

                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        // -------------------------
        // CARD CLICK
        // -------------------------
        holder.itemView.setOnClickListener(v -> {

            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);

        });

        // -------------------------
        // LIFT ANIMATION
        // -------------------------
        holder.itemView.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                v.animate()
                        .translationY(-8)
                        .setDuration(120)
                        .start();

            } else if (event.getAction() == MotionEvent.ACTION_UP ||
                    event.getAction() == MotionEvent.ACTION_CANCEL) {

                v.animate()
                        .translationY(0)
                        .setDuration(120)
                        .start();
            }

            return false;
        });

    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {

        TextView jobTitle, companyName, jobLocation, jobSalary, jobSchedule;
        ImageButton saveJob, removeJob;
        CardView cardView;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);

            jobTitle = itemView.findViewById(R.id.jobTitle);
            companyName = itemView.findViewById(R.id.companyName);
            jobLocation = itemView.findViewById(R.id.jobLocation);
            jobSalary = itemView.findViewById(R.id.jobSalary);
            jobSchedule = itemView.findViewById(R.id.jobSchedule);

            saveJob = itemView.findViewById(R.id.saveJob);
            removeJob = itemView.findViewById(R.id.removeJob);
            cardView = itemView.findViewById(R.id.jobCard);
        }
    }
}