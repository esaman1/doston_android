package com.dinosoftlabs.tictic.Gif;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinosoftlabs.tictic.MessageChat_Classes.Gif_Adapter;
import com.dinosoftlabs.tictic.R;
import com.dinosoftlabs.tictic.SimpleClasses.Fragment_Callback;
import com.dinosoftlabs.tictic.SimpleClasses.Variables;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.MediaType;
import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.giphy.sdk.core.network.response.ListMediaResponse;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class Gif_fragment_BottomSheet  extends BottomSheetDialogFragment {

    View view;
    Context context;
//    RecyclerView recyclerView;
    ProgressBar progressBar;
    Fragment_Callback fragment_callback;
    Gif_Adapter gif_adapter;
    final ArrayList<String> url_list=new ArrayList<>();
    RecyclerView gips_list;
    GPHApi client = new GPHApiClient(Variables.gif_api_key1);
    EditText search_edit;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.bottom_gif_layout, container, false);
        context=getContext();

        progressBar = view.findViewById(R.id.progress_bar_gif);
        search_edit = view.findViewById(R.id.search_edit);
        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    progressBar.setVisibility(View.VISIBLE);
                    searchGif(search_edit.getText().toString());
                    return true;
                }
                return false;
            }
        });
        GetGipy();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    public void searchGif(String search){
        /// Gif Search
        client.search(search, MediaType.gif, null, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                if (result == null) {
                    // Do what you want to do with the error
                } else {
                    if (result.getData() != null) {
                        url_list.clear();
                        for (Media gif : result.getData()) {
                            url_list.add(gif.getId());
                            gif_adapter.notifyDataSetChanged();
                        }
                        gips_list.smoothScrollToPosition(0);
                        progressBar.setVisibility(View.GONE);

                    } else {
                        Log.e("giphy error", "No results found");
                    }
                }
            }
        });
    }
    public void GetGipy() {
        url_list.clear();
        gips_list = view.findViewById(R.id.recylerview_gif);
        gips_list.setLayoutManager(new GridLayoutManager(context,3));
        gif_adapter = new Gif_Adapter(context, url_list, new Gif_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(String item) {
//                SendGif(item);
//                slideDown();
                Bundle bundle = new Bundle();
                bundle.putString("action", "sendGif");
                bundle.putString("gif_item", item);
                dismiss();
                fragment_callback.Responce(bundle);

            }
        });
        gips_list.setAdapter(gif_adapter);

        client.trending(MediaType.gif, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(ListMediaResponse result, Throwable e) {
                if (result == null) {
                    // Do what you want to do with the error
                } else {
                    if (result.getData() != null) {
                        for (Media gif : result.getData()) {

                            url_list.add(gif.getId());
                        }
                        gif_adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Log.e("giphy error", "No results found");
                    }
                }
            }
        });
    }

    public Gif_fragment_BottomSheet() {
    }

    public Gif_fragment_BottomSheet(Fragment_Callback fragment_callback) {
        this.fragment_callback = fragment_callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }


}
