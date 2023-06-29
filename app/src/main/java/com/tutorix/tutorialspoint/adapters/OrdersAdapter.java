package com.tutorix.tutorialspoint.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.activities.OrderDetailsActivity;
import com.tutorix.tutorialspoint.models.MyOrders;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrdersAdapter  extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>{

    List<MyOrders>listOrders;
    MyOrders order;
    public OrdersAdapter()
    {
        listOrders=new ArrayList<>();
    }
    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OrdersViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_order_item_layout,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder viewholder, final int i) {
        order=listOrders.get(i);
        viewholder.txt_order_title.setText(getClassname(order.class_id)+" ( "+order.order_number+" )");
        viewholder.order_amount.setText(getCurrency(order.order_currency)+order.total_amount);

        viewholder.order_date.setText(order.created_dtm);
        if(order.order_activation_type.equalsIgnoreCase("O"))
        {

            viewholder.img_item.setImageResource(0);
            viewholder.order_txt.setText("Ordered for Online Streaming");
        }else
        if(order.order_activation_type.equalsIgnoreCase("S"))
        {
            viewholder.img_item.setImageResource(R.drawable.ic_sd_card);
            viewholder.order_txt.setText("Ordered for SD Card");
        }else
        {
            viewholder.img_item.setImageResource(R.drawable.tablet);
            viewholder.order_txt.setText("Ordered for Tablet");
        }
        viewholder.order_status.setText(getOrderStatus(order.order_status));
        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(v.getContext(), OrderDetailsActivity.class);
                in.putExtra("order_id",listOrders.get(i).order_id);
                v.getContext().startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOrders.size();
    }
    public void setOrders(List<MyOrders>listOrder)
    {
        clearOrders();
        listOrders.addAll(listOrder);
        notifyDataSetChanged();
    }
    public void clearOrders()
    {
        if(listOrders!=null)
        listOrders.clear();

    }

    class OrdersViewHolder extends RecyclerView.ViewHolder{

        TextView txt_order_title;
        TextView order_amount;
        TextView order_date;
        TextView order_status;
        TextView order_txt;
        ImageView img_item;
        View itemView;
        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;
            txt_order_title=itemView.findViewById(R.id.txt_order_title);
            order_amount=itemView.findViewById(R.id.order_amount);
            order_date=itemView.findViewById(R.id.order_date);
            order_status=itemView.findViewById(R.id.order_status);
            img_item=itemView.findViewById(R.id.img_item);
            order_txt=itemView.findViewById(R.id.order_txt);
        }
    }

    private String getClassname(String classname)
    {
        if (classname.equalsIgnoreCase("1")) {
            return  "Class 6";
        } else if (classname.equalsIgnoreCase("2")) {
           return "Class 7";
        } else if (classname.equalsIgnoreCase("3")) {
            return "Class 8";
        } else if (classname.equalsIgnoreCase("4")) {
            return  "Class 9";
        }
        return "";
    }

    private String getCurrency(String currency)
    {
        if (currency.equalsIgnoreCase("U")) {
            return  "$ ";
        }
        return "Rs ";
    }

    private String getOrderStatus(String currency)
    {
       /* if(true)
        return currency;*/
        if (currency.equalsIgnoreCase("DL")) {
            return  "Delivered ";
        }
        else if (currency.equalsIgnoreCase("DP")) {
            return  "Dispatched ";
        }else if (currency.equalsIgnoreCase("OP")) {
            return  "Order Placed ";
        }else if (currency.equalsIgnoreCase("PR")) {
            return  "Preparing ";
        }else if (currency.equalsIgnoreCase("CA")) {
            return  "Cancelled ";
        }
        return " ";
    }
    private String getOrderType(String currency)
    {
        if (currency.equalsIgnoreCase("DL")) {
            return  "Delivered ";
        }
        else if (currency.equalsIgnoreCase("DP")) {
            return  "Dispatched ";
        }else if (currency.equalsIgnoreCase("OP")) {
            return  "Order Placed ";
        }else if (currency.equalsIgnoreCase("PR")) {
            return  "Preparing ";
        }else if (currency.equalsIgnoreCase("CL")) {
            return  "Cancelled ";
        }
        return " ";
    }
}
