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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.devspark.progressfragment.ProgressFragment;
import com.possebom.openwifipasswordrecover.R;
import com.possebom.openwifipasswordrecover.adapter.NetworkAdapter;
import com.possebom.openwifipasswordrecover.interfaces.NetworkListener;
import com.possebom.openwifipasswordrecover.model.Network;
import com.possebom.openwifipasswordrecover.parser.NetworkParser;

import java.util.List;

/**
 * Created by alexandre on 20/02/14.
 */
public class NetworkFragment extends ProgressFragment implements SearchView.OnQueryTextListener, NetworkListener {
    public static final String TAG = "NetworkFragment";
    private NetworkAdapter mAdapter;
    private SearchView mSearchView;
    private ListView listView;
    private View contentView;

    public NetworkFragment() {
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(contentView);
        setEmptyText(R.string.no_data);
    }

    @Override
    public final void onResume() {
        setContentShown(false);
        new NetworkParser(getActivity(), this).execute();
        super.onResume();
    }

    @Override
    public final boolean onQueryTextSubmit(String s) {
        if (mSearchView != null) {
            mSearchView.clearFocus();
        }
        return true;
    }

    @Override
    public final boolean onQueryTextChange(String s) {
        if (mAdapter != null) {
            mAdapter.getFilter().filter(s);
        }
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
        if (id == R.id.action_about) {
            final Fragment fragmentAbout = new AboutFragment();
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragmentAbout)
                    .addToBackStack(TAG)
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public final View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstState) {
        contentView = inflater.inflate(R.layout.fragment_main, container, false);

        listView = (ListView) contentView.findViewById(R.id.listview);
        listView.setFastScrollEnabled(true);
        listView.setEmptyView(contentView.findViewById(R.id.nodataview));

        return super.onCreateView(inflater, container, savedInstState);
    }

    @Override
    public final void onParserDone(List<Network> networkList) {
        if (networkList.isEmpty()) {
            setContentEmpty(true);
        } else {
            mAdapter = new NetworkAdapter(getActivity(), networkList);
            listView.setAdapter(mAdapter);
            if (networkList.size() > 0 && networkList.get(0).isConnected()) {
                listView.setItemChecked(0, true);
            }
        }
        setContentShown(true);
    }
}
