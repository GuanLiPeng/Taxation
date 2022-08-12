package com.example.taxation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taxation.R;
import com.example.taxation.bean.GroupChildListItem;
import com.example.taxation.bean.OneTextItem;

import java.util.List;

/**
 * 下拉分组列表 的适配器
 */
public class GroupListAdapter extends BaseExpandableListAdapter {

    private final List<OneTextItem> groupList;
    private final List<List<GroupChildListItem>> childList;
    private final Context mContext;

    private static class ViewHolderGroup {
        TextView groupTitle;
        ImageView groupArrow;
    }

    private static class ViewHolderChild {
        TextView childTitle, startInfo, endInfo;
    }


    /**
     * 构造函数
     *
     * @param groupList
     * @param childList
     * @param mContext
     */
    public GroupListAdapter(List<OneTextItem> groupList, List<List<GroupChildListItem>> childList, Context mContext) {
        this.groupList = groupList;
        this.childList = childList;
        this.mContext = mContext;
    }

    /**
     * 取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
     *
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderGroup groupHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_group, parent, false);
            groupHolder = new ViewHolderGroup();

            //绑定视图
            groupHolder.groupTitle = convertView.findViewById(R.id.l_i_g_title);
            groupHolder.groupArrow = convertView.findViewById(R.id.l_i_g_arrow_icon);

            convertView.setTag(groupHolder);
        } else {
            groupHolder = (ViewHolderGroup) convertView.getTag();
        }

        //设置标题文字
        groupHolder.groupTitle.setText(groupList.get(groupPosition).getTitle());
        //设置图标
        groupHolder.groupArrow.setImageResource(isExpanded ? R.drawable.ic_up_arrow : R.drawable.ic_down_arrow);
        return convertView;
    }

    /**
     * 取得显示给定分组给定子位置的数据用的视图
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChild groupChildHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_group_child, parent, false);
            groupChildHolder = new ViewHolderChild();

            //视图绑定
            groupChildHolder.childTitle = convertView.findViewById(R.id.l_i_g_c_title);
            groupChildHolder.startInfo = convertView.findViewById(R.id.l_i_g_c_start_info);
            groupChildHolder.endInfo = convertView.findViewById(R.id.l_i_g_c_end_info);

            convertView.setTag(groupChildHolder);
        } else {
            groupChildHolder = (ViewHolderChild) convertView.getTag();
        }

        //修改textView的内容
        GroupChildListItem item = childList.get(groupPosition).get(childPosition);
        groupChildHolder.childTitle.setText(item.getChildTitle());
        groupChildHolder.startInfo.setText(item.getStartInfo());
        groupChildHolder.endInfo.setText(item.getEndInfo());
        return convertView;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
