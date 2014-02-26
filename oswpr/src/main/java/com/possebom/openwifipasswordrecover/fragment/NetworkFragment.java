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

package com.possebom.openwifipasswordrecover.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.possebom.openwifipasswordrecover.interfaces.NetworkListener;
import com.possebom.openwifipasswordrecover.R;
import com.possebom.openwifipasswordrecover.adapter.NetworkAdapter;
import com.possebom.openwifipasswordrecover.model.Network;
import com.possebom.openwifipasswordrecover.parser.NetworkParser;

import java.util.List;

/**
 * Created by alexandre on 20/02/14.
 */
public class NetworkFragment extends Fragment implements SearchView.OnQueryTextListener,NetworkListener {
    private NetworkAdapter mAdapter;
    private SearchView mSearchView;
    private ListView listView;
    private Context context;

    public NetworkFragment() {
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public final void onResume() {
        new NetworkParser(context, this).execute();
        super.onResume();
    }

    @Override
    public final boolean onQueryTextSubmit(String s) {
        mSearchView.clearFocus();
        return true;
    }

    @Override
    public final boolean onQueryTextChange(String s) {
        mAdapter.getFilter().filter(s);
        return true;
    }

    @Override
    public final void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            mSearchView.setIconified(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public final View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        final View viewNoData = view.findViewById(R.id.nodataview);

        listView = (ListView) view.findViewById(R.id.listview);
        listView.setFastScrollEnabled(true);
        listView.setEmptyView(viewNoData);

        return view;
    }

    @Override
    public final void onParserDone(List<Network> networkList) {
        mAdapter = new NetworkAdapter(context,networkList);
        listView.setAdapter(mAdapter);
        if(networkList.size() > 0 && networkList.get(0).isConnected()){
            listView.setItemChecked(0, true);
        }
    }
}
