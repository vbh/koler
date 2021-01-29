package com.chooloo.www.callmanager.ui.recents;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chooloo.www.callmanager.cursorloader.RecentsCursorLoader;
import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.entity.RecentCall;
import com.chooloo.www.callmanager.ui.cursor.CursorAdapter;
import com.chooloo.www.callmanager.ui.listitem.ListItem;
import com.chooloo.www.callmanager.ui.listitem.ListItemHolder;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.RelativeTime;
import com.chooloo.www.callmanager.util.Utilities;

import static com.chooloo.www.callmanager.util.AnimationUtils.setFadeUpAnimation;

public class RecentsAdapter extends CursorAdapter<ListItemHolder> {

    private OnRecentItemClickListener mOnRecentItemClickListener;
    private OnRecentItemLongClickListener mOnRecentItemLongClickListener;

    public RecentsAdapter(Context context) {
        super(context);
        mOnRecentItemClickListener = recentCall -> {
        };
        mOnRecentItemLongClickListener = recentCall -> true;
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListItemHolder(new ListItem(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        mCursor.moveToPosition(position);
        RecentCall recentCall = RecentsCursorLoader.getRecentCallFromCursor(mCursor);
        Contact callerContact = ContactUtils.lookupContact(mContext, recentCall.getCallerNumber());
        String callDate = recentCall.getCallDate() != null ? RelativeTime.getTimeAgo(recentCall.getCallDate().getTime()) : null;

        ListItem listItem = holder.getListItem();
        listItem.setBigText(callerContact.name == null ? recentCall.getCallerNumber() : callerContact.name);
        listItem.setSmallText(callDate);
        listItem.setImageDrawable(ContextCompat.getDrawable(mContext, Utilities.getCallTypeImage(recentCall.getCallType())));
        listItem.setOnClickListener(view -> mOnRecentItemClickListener.onRecentItemClick(recentCall));
        listItem.setOnLongClickListener(view -> mOnRecentItemLongClickListener.onRecentItemLongClick(recentCall));
        setFadeUpAnimation(listItem);
    }

    public void setOnRecentItemClickListener(OnRecentItemClickListener onRecentItemClickListener) {
        mOnRecentItemClickListener = onRecentItemClickListener;
    }

    public void setOnRecentItemLongClickListener(OnRecentItemLongClickListener onRecentItemLongClickListener) {
        mOnRecentItemLongClickListener = onRecentItemLongClickListener;
    }

    public interface OnRecentItemClickListener {
        void onRecentItemClick(RecentCall recentCall);

    }

    public interface OnRecentItemLongClickListener {
        boolean onRecentItemLongClick(RecentCall recentCall);
    }

}
