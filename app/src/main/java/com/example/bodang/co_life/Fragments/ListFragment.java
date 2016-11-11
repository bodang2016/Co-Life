package com.example.bodang.co_life.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
    private Boolean[] checkresult={false,false};
    private String[] pending;

    private View main;
    private EditText name;
    private EditText password;
    private Button submit;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView listView;
    ArrayList<HashMap<String, String>> items;
    private SimpleAdapter adapter;
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
        // Inflate the layout for this fragment
        main = inflater.inflate(R.layout.fragment_list, container, false);
        name = (EditText) main.findViewById(R.id.name);
        password = (EditText) main.findViewById(R.id.password);
        submit = (Button) main.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String[] str = new String[2];
                        int result = client.Init();
                        if(result == 1) {
                            try {
                                checkresult = client.Login(name.getText().toString(), password.getText().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            myHandler.sendEmptyMessage(LOGIN);
                        }
                    }
                }).start();
            }
        });
        this.showlist();
        return main;
    }
    public void showlist(){
        ArrayList<User> users=new ArrayList<User>();
        User u1=new User();
        u1.setUserName("Bo");
        users.add(u1);
        User u2=new User();
        u2.setUserName("BoYu");
        users.add(u2);
        for(int j=0; j<20;j++){
            User u3=new User();
            u1.setUserName("Boo");
            users.add(u3);
        }

        //deal with the listView
        listView = (ListView)main.findViewById(R.id.listview1);
        items = new ArrayList<HashMap<String, String>>();
        for(int i=0; i<users.size();i++){
            User u=users.get(i);
            HashMap<String, String> map = new HashMap<String, String>();
            //add text into map
            map.put("name",u.getUserName());
            //add map to list
            items.add(map);
        }
//        for (User u : users) {
//            HashMap<String, String> map = new HashMap<String, String>();
//            //add text into map
//            map.put("name",u.getUserName());
//            //add map to list
//            items.add(map);
//        }
        adapter = new SimpleAdapter(main.getContext(),items, // listItems
                R.layout.user_item,  //item layout
                new String[] {"name"},  //strings
                new int[ ] {R.id.textView2}  );  //TextView ID in item layout
//        //set the height of the listview
//        int num=adapter.getCount();
//        if(num>0){
//            View i=adapter.getView(0,null,listView);
//            i.measure(0,0);//This line is known by me from the Internet.
//            ViewGroup.LayoutParams lp=listView.getLayoutParams();
//            lp.height=(int) ((int)i.getMeasuredHeight()*num*1.2);
//            listView.setLayoutParams(lp);
//        }
        listView.setAdapter(adapter);
    }

    private final static int DO_CHANGENAME = 0;
    private final static int LOGIN=1;
    private final Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            final int what = msg.what;
            switch (what) {
                case DO_CHANGENAME:
                    changeName(pending);
                    break;
                case LOGIN:
                    if(checkresult[1]){
                        String[] a={"newuser create","new password create",""};
                        changeName(a);
                    }else{
                        if(checkresult[0]){
                            String[] a={"username correct","password correct","password correct"};
                            changeName(a);
                        }else{
                            String[] a={" ","wrong password","wrong"};
                            changeName(a);
                        }
                    }
                    break;
            }
        }
    };

    public void changeName(String[] str) {
        name.setText(str[0]);
        password.setText(str[1]);
    }



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
}
