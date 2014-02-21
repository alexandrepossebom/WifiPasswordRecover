/*
 * Copyright Alexandre Possebom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.possebom.openwifipasswordrecover.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.possebom.openwifipasswordrecover.MyApplication;
import com.possebom.openwifipasswordrecover.R;
import com.possebom.openwifipasswordrecover.model.Network;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by alexandre on 17/02/14.
 */
public class NetworkAdapter extends BaseAdapter implements SectionIndexer, Filterable {
    private static final String SECTIONS = "abcdefghilmnopqrstuvz";
    private final Context context;
    private final MyApplication myApplication;
    private Filter networkFilter;
    private List<Network> list;

    public NetworkAdapter(final Context context) {
        super();
        this.context = context;
        myApplication = (MyApplication) context.getApplicationContext();
        list = myApplication.getList();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Network getItem(final int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View mConvertView = null;
        final Network network = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mConvertView = layoutInflater.inflate(R.layout.row, parent, false);
            holder = new ViewHolder();
            holder.tvSsid = (TextView) mConvertView.findViewById(R.id.tv_ssid);
            holder.tvPassword = (TextView) mConvertView.findViewById(R.id.tv_password);
            mConvertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            mConvertView = convertView;
        }

        holder.tvSsid.setText(network.getSsid());
        holder.tvPassword.setText(network.getPassword());


        if(network.compareTo(myApplication.getCurrentNetwork()) == 0){
            mConvertView.setBackgroundResource(R.drawable.bg_card_selected);
        }else{
            mConvertView.setBackgroundResource(R.drawable.bg_card);
        }

        return mConvertView;
    }

    // Code from survivingwithandroid.com
    @Override
    public Object[] getSections() {
        final Character[] sectionsArr = new Character[SECTIONS.length()];
        for (int i = 0; i < SECTIONS.length(); i++) {
            sectionsArr[i] = SECTIONS.charAt(i);
        }
        return sectionsArr;
    }

    // Code from survivingwithandroid.com
    @Override
    public int getPositionForSection(int sectionIndex) {
        int ret = 0;
        for (int i = 0; i < this.getCount(); i++) {
            final String ssid = this.getItem(i).getSsid().toLowerCase(Locale.getDefault());
            if (ssid.charAt(0) == SECTIONS.charAt(sectionIndex)) {
                ret = i;
                break;
            }
        }
        return ret;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Filter getFilter() {
        if (networkFilter == null){
            networkFilter = new NetworkFilter();
        }
        return networkFilter;
    }

    private static class ViewHolder {
        private TextView tvSsid;
        private TextView tvPassword;
    }


    private class NetworkFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final FilterResults results = new FilterResults();
            list = myApplication.getList();
            if (constraint == null || constraint.length() == 0) {
                results.values = list;
                results.count = list.size();
            } else {
                final List<Network> mList = new ArrayList<Network>();
                for (Network network : list) {
                    if (network.getSsid().toLowerCase().contains(constraint.toString().toLowerCase())
                            || network.getPassword().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        mList.add(network);
                    }
                }
                results.values = mList;
                results.count = mList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<Network>) results.values;
                notifyDataSetChanged();
        }

    }
}
