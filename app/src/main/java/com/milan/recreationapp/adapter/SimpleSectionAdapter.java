package com.milan.recreationapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class SimpleSectionAdapter<T> extends BaseAdapter {
    // Debug
    static final boolean DEBUG = false;
    static final String TAG = SimpleSectionAdapter.class.getSimpleName();

    // Constants
    private static final int VIEW_TYPE_SECTION_HEADER = 0;

    // Attributes
    private Context mContext;
    private BaseAdapter mListAdapter;
    private int mSectionHeaderLayoutId;
    private int mSectionTitleTextViewId;
    private Sectionizer<T> mSectionizer;
    private LinkedHashMap<String, Integer> mSections;

    /**
     * Constructs a {@linkplain SimpleSectionAdapter}.
     * 
     * @param context The context for this adapter.
     * @param sectionHeaderLayoutId Layout Id of the layout that is to be used for the header.
     * @param sectionTitleTextViewId Id of a TextView present in the section header layout.
     */
    public SimpleSectionAdapter(Context context, BaseAdapter listAdapter, 
            int sectionHeaderLayoutId, int sectionTitleTextViewId, 
            Sectionizer<T> sectionizer) {
        if(context == null) {
            throw new IllegalArgumentException("context cannot be null.");
        } else if(listAdapter == null) {
            throw new IllegalArgumentException("listAdapter cannot be null.");
        } else if(sectionizer == null) {
            throw new IllegalArgumentException("sectionizer cannot be null.");
        } else if(!isTextView(context, sectionHeaderLayoutId, sectionTitleTextViewId)) {
            throw new IllegalArgumentException("sectionTitleTextViewId should be a TextView.");
        }

        this.mContext = context;
        this.mListAdapter = listAdapter;
        this.mSectionHeaderLayoutId = sectionHeaderLayoutId;
        this.mSectionTitleTextViewId = sectionTitleTextViewId;
        this.mSectionizer = sectionizer;
        this.mSections = new LinkedHashMap<String, Integer>();

        // Find sections
        findSections();
    }

    private boolean isTextView(Context context, int layoutId, int textViewId) {
        View inflatedView = View.inflate(context, layoutId, null);
        View foundView = inflatedView.findViewById(textViewId);

        return foundView instanceof TextView;
    }

    @Override
    public int getCount() {
        return mListAdapter.getCount() + getSectionCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        SectionHolder sectionHolder = null;

        switch (getItemViewType(position)) {
        case VIEW_TYPE_SECTION_HEADER:
            if(view == null) {
                view = View.inflate(mContext, mSectionHeaderLayoutId, null);

                sectionHolder = new SectionHolder();
                sectionHolder.titleTextView = (TextView) view.findViewById(mSectionTitleTextViewId);

                view.setTag(sectionHolder);
            } else {
                sectionHolder = (SectionHolder) view.getTag();
            }
            break;

        default:
            view = mListAdapter.getView(getIndexForPosition(position), 
                    convertView, parent);
            break;
        }

        if(sectionHolder != null) {
            String sectionName = sectionTitleForPosition(position);
            sectionHolder.titleTextView.setText(sectionName);
        }

        return view;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mListAdapter.areAllItemsEnabled() ? 
                mSections.size() == 0 : false;
    }

    @Override
    public int getItemViewType(int position) {
        int positionInCustomAdapter = getIndexForPosition(position);
        return mSections.values().contains(position) ? 
                VIEW_TYPE_SECTION_HEADER : 
                    mListAdapter.getItemViewType(positionInCustomAdapter) + 1;
    }

    @Override
    public int getViewTypeCount() {
        return mListAdapter.getViewTypeCount() + 1;
    }

    @Override
    public boolean isEnabled(int position) {
        return mSections.values().contains(position) ? 
                false : mListAdapter.isEnabled(getIndexForPosition(position));
    }

    @Override
    public Object getItem(int position) {
        return mListAdapter.getItem(getIndexForPosition(position));
    }

    @Override
    public long getItemId(int position) {
        return mListAdapter.getItemId(getIndexForPosition(position));
    }

    @Override
    public void notifyDataSetChanged() {
        mListAdapter.notifyDataSetChanged();
        findSections();
        super.notifyDataSetChanged();
    }

    /**
     * Returns the actual index of the object in the data source linked to the this list item.
     * 
     * @return Index of the item in the wrapped list adapter's data source.
     */
    public int getIndexForPosition(int position) {
        int nSections = 0;

        Set<Map.Entry<String, Integer>> entrySet = mSections.entrySet();
        for(Map.Entry<String, Integer> entry : entrySet) {
            if(entry.getValue() < position) {
                nSections++;
            }
        }

        return position - nSections;
    }

    static class SectionHolder {
        public TextView titleTextView;
    }

    private void findSections() {
        int n = mListAdapter.getCount();
        int nSections = 0;
        mSections.clear();

        for(int i=0; i<n; i++) {
            String sectionName = mSectionizer.getSectionTitleForItem((T) mListAdapter.getItem(i));

            if(!mSections.containsKey(sectionName)) {
                mSections.put(sectionName, i + nSections);
                nSections ++;
            }
        }

        if(DEBUG) {
            Log.d(TAG, String.format("Found %d sections.", mSections.size()));
        }
    }

    private int getSectionCount() {
        return mSections.size();
    }

    private String sectionTitleForPosition(int position) {
        String title = null;

        Set<Map.Entry<String, Integer>> entrySet = mSections.entrySet();
        for(Map.Entry<String, Integer> entry : entrySet) {
            if(entry.getValue() == position) {
                title = entry.getKey();
                break;
            }
        }
        return title;
    }

}