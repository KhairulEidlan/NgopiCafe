package com.example.ngopi.apps.rv;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ngopi.R;
import com.example.ngopi.apps.model.RvCart;
import com.example.ngopi.apps.model.RvOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class RvOrderHistoryAdapter extends RecyclerView.Adapter<RvOrderHistoryAdapter.RvOrderHistoryHolder> {
    private final Context context;
    private final ArrayList<RvOrder> orderItem;

    private View view;
    private Dialog orderDialog;

    private FirebaseFirestore db;

    public RvOrderHistoryAdapter(Context context, ArrayList<RvOrder> orderItem) {
        this.context = context;
        this.orderItem = orderItem;
    }

    @NonNull
    @Override
    public RvOrderHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_app_order_history,parent,false);

        db = FirebaseFirestore.getInstance();
        orderDialog = new Dialog(view.getContext());

        return new RvOrderHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvOrderHistoryHolder holder, int position) {
        RvOrder currentItem = orderItem.get(position);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(currentItem.getOrderDate(), formatter);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM yyyy");

        holder.lblOrderNo.append(currentItem.getOrderNo());
        holder.lblOrderDate.append(dtf.format(date));
        holder.lblOrderPickUp.append(currentItem.getOrderPickUp());
        holder.lblOrderStatus.append(currentItem.getOrderStatus());

        holder.cardOrderInfo.setOnClickListener(v -> dialogInfo(currentItem));

    }

    private void dialogInfo(RvOrder currentItem) {
        view = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_app_order,null);

        orderDialog.setContentView(view);
        orderDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.dialog_background));
        orderDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        orderDialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView lblOrderNo, lblOrderAmount;
        TableLayout tblOrderDetail;
        Button btnClose;

        lblOrderNo = view.findViewById(R.id.lblOrderNo);
        tblOrderDetail = view.findViewById(R.id.tblOrderDetail);
        lblOrderAmount = view.findViewById(R.id.lblOrderAmount);
        btnClose = view.findViewById(R.id.btnClose);

        lblOrderNo.setText(currentItem.getOrderNo());

        db.collection("Order")
                .document(currentItem.getOrderId())
                .collection("Order Details")
                .get()
                .addOnCompleteListener(taskOrder -> {
                    if(taskOrder.isSuccessful()){
                        for (QueryDocumentSnapshot documentOrder: taskOrder.getResult()){
                            TableRow tblRowOrderDetail = new TableRow(view.getContext());
                            TextView txtOrderName = new TextView(view.getContext());
                            TextView txtOrderTotal = new TextView(view.getContext());

                            db.collectionGroup("Menu")
                                    .get()
                                    .addOnCompleteListener(taskOrderDetail -> {
                                        if (taskOrderDetail.isSuccessful()){
                                            for (QueryDocumentSnapshot documentOrderDetail : taskOrderDetail.getResult()){
                                                if(documentOrderDetail.getId().equals(documentOrder.getData().get("menuId"))){
                                                    txtOrderName.setText(
                                                            documentOrderDetail.getData().get("menu_name").toString() + " ("
                                                                    + documentOrder.getData().get("type") + ") (x" + documentOrder.getData().get("quantity") + ") "
                                                    );
                                                    txtOrderName.setTextColor(Color.BLACK);
                                                    txtOrderName.setGravity(Gravity.LEFT);
                                                    txtOrderName.setTextSize(14);
                                                }
                                            }
                                        }
                                    });
                            tblRowOrderDetail.addView(txtOrderName);

                            Double total = Double.parseDouble(documentOrder.getData().get("price").toString())*Double.parseDouble(documentOrder.getData().get("quantity").toString());
                            txtOrderTotal.setText(String.format(Locale.getDefault(),"RM %.2f",total));
                            txtOrderTotal.setTextColor(Color.BLACK);
                            txtOrderTotal.setGravity(Gravity.CENTER);
                            txtOrderTotal.setTextSize(14);
                            tblRowOrderDetail.addView(txtOrderTotal);

                            tblOrderDetail.addView(tblRowOrderDetail);
                        }
                    }
                });

        lblOrderAmount.append("RM "+currentItem.getOrderAmount());

        btnClose.setOnClickListener(v -> orderDialog.dismiss());

        orderDialog.setCancelable(false);
        orderDialog.show();
    }

    @Override
    public int getItemCount() {
        return orderItem == null ? 0:orderItem.size();
    }

    public static class RvOrderHistoryHolder extends RecyclerView.ViewHolder{
        MaterialCardView cardOrderInfo;
        TextView lblOrderNo, lblOrderDate, lblOrderPickUp, lblOrderStatus;

        public RvOrderHistoryHolder(@NonNull View itemView) {
            super(itemView);
            cardOrderInfo = itemView.findViewById(R.id.cardOrderInfo);
            lblOrderNo = itemView.findViewById(R.id.lblOrderNo);
            lblOrderDate = itemView.findViewById(R.id.lblOrderDate);
            lblOrderPickUp = itemView.findViewById(R.id.lblOrderPickUp);
            lblOrderStatus = itemView.findViewById(R.id.lblOrderStatus);
        }
    }
}
