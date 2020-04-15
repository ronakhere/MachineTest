package com.ronaktest.myapp.ui.fetchContacts;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ronaktest.myapp.R;
import com.ronaktest.myapp.model.Contact;
import com.ronaktest.myapp.widgets.RoundedImageView;
import com.squareup.picasso.Picasso;


public class ContactViewHolder extends RecyclerView.ViewHolder {
    private de.hdodenhof.circleimageview.CircleImageView mImage;
    private TextView mLabel;
    private Contact mBoundContact;

    public ContactViewHolder(final View itemView) {
        super(itemView);
        mImage = (de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.rounded_iv_profile);
        mLabel = (TextView) itemView.findViewById(R.id.tv_label);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBoundContact != null) {
                    Toast.makeText(
                            itemView.getContext(),
                              mBoundContact.name,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void bind(Contact contact) {
        mBoundContact = contact;
        mLabel.setText(contact.name);
        Picasso.get().load(contact.profilePic).placeholder(R.drawable.ic_person_black_24dp)
                .error(R.drawable.ic_person_black_24dp).into(mImage);
    }
}
