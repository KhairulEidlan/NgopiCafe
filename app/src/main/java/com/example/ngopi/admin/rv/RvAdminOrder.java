package com.example.ngopi.admin.rv;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ngopi.R;
import com.example.ngopi.apps.model.RvOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;


public class RvAdminOrder extends RecyclerView.Adapter<RvAdminOrder.RvAdminOrderHolder> {
    private Context context;
    private ArrayList<RvOrder> rcOrder;


    private View view;
    private Dialog orderDialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public RvAdminOrder(Context context, ArrayList<RvOrder> rcOrder) {
        this.context = context;
        this.rcOrder = rcOrder;

    }
    @NonNull
    @Override
    public RvAdminOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_app_order_history,parent,false);

        orderDialog = new Dialog(view.getContext());
        return new RvAdminOrderHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RvAdminOrder.RvAdminOrderHolder holder, int position) {
        RvOrder currentItem = rcOrder.get(position);

        holder.lblOrderNo.setText(currentItem.getOrderNo());
        holder.lblOrderDate.setText(currentItem.getOrderDate());
        holder.lblOrderPickUp.setText(currentItem.getOrderPickUp());
        holder.lblOrderStatus.setText(currentItem.getOrderStatus());

        holder.cardOrderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInfo(currentItem);

            }
        });

    }

    private void dialogInfo(RvOrder currentItem) {


        view = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_admin_order,null);

        orderDialog.setContentView(view);
        orderDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.dialog_background));
        orderDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        orderDialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView lblOrderNo, lblOrderAmount, lblOrderuser;
        TableLayout tblOrderDetail;
        Button btnCancel, btnComplete;

        lblOrderNo = view.findViewById(R.id.lblOrderNo);
        lblOrderuser = view.findViewById(R.id.lblOrderuser);
        tblOrderDetail = view.findViewById(R.id.tblOrderDetail);
        lblOrderAmount = view.findViewById(R.id.lblOrderAmount);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnComplete = view.findViewById(R.id.btnComplete);

        lblOrderNo.setText(currentItem.getOrderNo());
        getusername(currentItem,lblOrderuser);

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

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = db.collection("Order").document(currentItem.getOrderId());
                documentReference
                        .update(
                                "status","Complete"
                        );

            }
        });

        btnCancel.setOnClickListener(v -> orderDialog.dismiss());

        orderDialog.setCancelable(false);
        orderDialog.show();
    }

    public void getusername(RvOrder currentItem, TextView lblOrderuser){

        DocumentReference userRef = db.collection("Users").document(currentItem.getuserid());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        lblOrderuser.setText(document.getData().get("username").toString());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rcOrder == null ? 0:rcOrder.size();
    }

    public static class RvAdminOrderHolder extends RecyclerView.ViewHolder{

        TextView lblOrderNo, lblOrderDate, lblOrderPickUp, lblOrderStatus;
        MaterialCardView cardOrderInfo;

        public RvAdminOrderHolder(@NonNull View orderview) {
            super(orderview);

            cardOrderInfo = itemView.findViewById(R.id.cardOrderInfo);
            lblOrderNo = orderview.findViewById(R.id.lblOrderNo);
            lblOrderDate = orderview.findViewById(R.id.lblOrderDate);
            lblOrderPickUp = orderview.findViewById(R.id.lblOrderPickUp);
            lblOrderStatus = orderview.findViewById(R.id.lblOrderStatus);
        }
    }
}