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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.possebom.openwifipasswordrecover.R;
import com.possebom.openwifipasswordrecover.adapter.NetworkAdapter;

/**
 * Created by alexandre on 20/02/14.
 */
public class NetworkFragment extends Fragment implements SearchView.OnQueryTextListener {
    private NetworkAdapter mAdapter;
    private SearchView mSearchView;

    public NetworkFragment() {
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        mSearchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mAdapter.getFilter().filter(s);
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            mSearchView.setIconified(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        final View viewNoData = view.findViewById(R.id.nodataview);

        mAdapter = new NetworkAdapter(getActivity());

        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(mAdapter);
        listView.setFastScrollEnabled(true);
        listView.setEmptyView(viewNoData);

        return view;
    }

}
