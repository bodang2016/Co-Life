package com.example.bodang.co_life.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bodang.co_life.Activities.MainActivity;
import com.example.bodang.co_life.Management.CustomListView;
import com.example.bodang.co_life.Management.Data;
import com.example.bodang.co_life.Management.LocalDatabaseHelper;
import com.example.bodang.co_life.Objects.User;
import com.example.bodang.co_life.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.bodang.co_life.Activities.MainActivity.client;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String[] pending;

    private View main;
    private LocalDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private CustomListView list;
    private ScrollView scrollView;
    private SimpleCursorAdapter adapter;
    private Cursor cursor;
    private SwipeRefreshLayout swipeLayout;
    private Boolean updateResult;
    private ArrayList<User> mainList;
    private TextView top;

    private updateGrouplistTask mupdateGrouplistTask = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        main = inflater.inflate(R.layout.fragment_list, container, false);
        scrollView = (ScrollView) main.findViewById(R.id.scrollView);
        dbHelper = new LocalDatabaseHelper(MainActivity.mainActivity, "localDatabase.db", null, 1);
        list = (CustomListView) main.findViewById(R.id.list_group);

        db = dbHelper.getReadableDatabase();
//        cursor = db.rawQuery("select * from localDatabase_info", null);
//        if (cursor.getCount() > 0) {
//            cursor.getCount();
//            inflateList(cursor);
//        }

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                swipeLayout.setEnabled(scrollView.getScrollY() == 0);
            }
        });
        swipeLayout = (SwipeRefreshLayout) main.findViewById(R.id.refresh_layout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mupdateGrouplistTask = new updateGrouplistTask(MainActivity.UnameValue);
                mupdateGrouplistTask.execute((Void) null);
            }
        });
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_red_dark);
        return main;
    }

    @Override
    public void onResume() {
        super.onResume();
        mupdateGrouplistTask = new updateGrouplistTask(MainActivity.UnameValue);
        mupdateGrouplistTask.execute((Void) null);
    }

//    private final static int DO_CHANGENAME = 0;
//    private final static int LOGIN = 1;
//    private final Handler myHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            final int what = msg.what;
//            switch (what) {
//                case DO_CHANGENAME:
//
//                    break;
//            }
//        }
//    };


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void inflateList(Cursor cursor) {
        adapter = new SimpleCursorAdapter(MainActivity.mainActivity, R.layout.list_item,
                cursor, new String[]{"name", "type", "time"}, new int[]{R.id.list_title, R.id.list_image, R.id.list_time},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
        setListViewHeightBasedOnChildren(list);
        list.setFocusable(false);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public class updateGrouplistTask extends AsyncTask<Void, Void, Boolean> {
        private final String mUsername;
        private ArrayList<User> groupList = null;

        public updateGrouplistTask(String userName) {
            super();
            mUsername = userName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeLayout.setRefreshing(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int result = client.Init();
            if (result == 1) {
                Data.deleteData(dbHelper.getReadableDatabase());
                groupList = client.groupList(mUsername);
                for (int i = 0; i < groupList.size(); i++) {
                    ContentValues values = new ContentValues();
                    values.put("name", groupList.get(i).getUserId());
                    values.put("type", R.drawable.tesco);
                    values.put("time", groupList.get(i).getTime());
                    Data.insertData(dbHelper.getReadableDatabase(), values);
                }
                cursor = db.rawQuery("select * from localDatabase_info", null);
                return true;
            }
            cursor = db.rawQuery("select * from localDatabase_info", null);
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            inflateList(cursor);
            adapter.notifyDataSetChanged();
            if(!success) {
                Toast.makeText(MainActivity.mainActivity, "No internet connection, local cache is loaded", Toast.LENGTH_SHORT).show();
            }
            swipeLayout.setRefreshing(false);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mupdateGrouplistTask = null;
        }
    }
}
