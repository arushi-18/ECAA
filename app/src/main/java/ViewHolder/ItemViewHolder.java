package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import Interface.ItemClickListener;
import com.example.ecaa.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,txtProductDescription,txtProductPrice,txtProductStatus;
    public ImageView imageView;
    public ItemClickListener listener;

    public ItemViewHolder(View itemView)
    {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
        txtProductStatus = (TextView) itemView.findViewById(R.id.product_status);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    public void onClick(View view)
    {
        listener.onClick(view, getAdapterPosition(), false);
    }

    //public Object getAdapterPosition() {
    //}
}
