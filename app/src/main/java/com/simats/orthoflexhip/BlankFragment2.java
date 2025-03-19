package com.simats.orthoflexhip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.simats.orthoflexhip.adapters.ApprovedAppointmentAdapter;
import com.simats.orthoflexhip.adapters.PendingAppointmentAdapter;
import com.simats.orthoflexhip.api.ApiService;
import com.simats.orthoflexhip.api.RetrofitClient;
import com.simats.orthoflexhip.dataClass.PendingAppoinmentData;
import com.simats.orthoflexhip.dataClass.PendingAppoinmentRecyclerData;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BlankFragment2 extends Fragment {

    RecyclerView appointmentrecyclerRV;
    TabLayout appointmentTabLayout;
    ApiService apiService = RetrofitClient.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.appoinments_fragment, container, false);

        appointmentrecyclerRV = view.findViewById(R.id.appointmentrecycler);
        appointmentTabLayout = view.findViewById(R.id.appointmentTabLayout);

        appointmentrecyclerRV.setLayoutManager(new LinearLayoutManager(requireContext()));

        if (appointmentTabLayout.getSelectedTabPosition() == 0) {
            fetchAllPendingAppointmentList();
        } else if (appointmentTabLayout.getSelectedTabPosition() == 1){
            fetchAllApprovedAppointmentList();
        }

        appointmentTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (appointmentTabLayout.getSelectedTabPosition() == 0) {
                    fetchAllPendingAppointmentList();
                } else if (appointmentTabLayout.getSelectedTabPosition() == 1){
                    fetchAllApprovedAppointmentList();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                System.out.println("tab unselected");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                System.out.println("tab reselected");
            }
        });

        return view;
    }

    private void fetchAllPendingAppointmentList() {
        apiService.pendingAppointment().enqueue(new Callback<PendingAppoinmentData>() {
            @Override
            public void onResponse(Call<PendingAppoinmentData> call, Response<PendingAppoinmentData> response) {
                if(response.isSuccessful()) {
                    if(response.body().getSuccess()) {
                        PendingAppointmentAdapter adapter = null;
                        PendingAppoinmentRecyclerData[] dataArray = response.body().getData();
                        ArrayList<PendingAppoinmentRecyclerData> dataList = new ArrayList<>(Arrays.asList(dataArray));
                        adapter = new PendingAppointmentAdapter(dataList, getContext());
                        appointmentrecyclerRV.setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(requireContext(), response.body().getError(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PendingAppoinmentData> call, Throwable t) {
                Toast.makeText(requireContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAllApprovedAppointmentList() {
        apiService.approvedAppointment().enqueue(new Callback<PendingAppoinmentData>() {
            @Override
            public void onResponse(Call<PendingAppoinmentData> call, Response<PendingAppoinmentData> response) {
                ApprovedAppointmentAdapter adapter = null;
                PendingAppoinmentRecyclerData[] dataArray = response.body().getData();
                ArrayList<PendingAppoinmentRecyclerData> dataList = new ArrayList<>(Arrays.asList(dataArray));
                adapter = new ApprovedAppointmentAdapter(dataList);
                appointmentrecyclerRV.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<PendingAppoinmentData> call, Throwable t) {
                Toast.makeText(requireContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}