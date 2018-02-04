package com.julioapps.commons.adapters

import android.content.pm.PackageManager
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.julioapps.commons.R
import com.julioapps.commons.extensions.formatSize
import com.julioapps.commons.extensions.getColoredDrawableWithColor
import com.julioapps.commons.extensions.isActivityDestroyed
import com.julioapps.commons.models.FileDirItem
import com.julioapps.commons.views.MyRecyclerView
import kotlinx.android.synthetic.main.filepicker_list_item.view.*

class FilepickerItemsAdapter(activity: com.julioapps.commons.activities.BaseSimpleActivity, val fileDirItems: List<FileDirItem>, recyclerView: MyRecyclerView,
                             itemClick: (Any) -> Unit) : com.julioapps.commons.adapters.MyRecyclerViewAdapter(activity, recyclerView, null, itemClick) {

    private val folderDrawable = activity.resources.getColoredDrawableWithColor(R.drawable.ic_folder, textColor)
    private val fileDrawable = activity.resources.getColoredDrawableWithColor(R.drawable.ic_file, textColor)

    init {
        folderDrawable.alpha = 180
        fileDrawable.alpha = 180
    }

    override fun getActionMenuId() = 0

    override fun prepareItemSelection(view: View) {}

    override fun markItemSelection(select: Boolean, view: View?) {}

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = createViewHolder(R.layout.filepicker_list_item, parent)

    override fun onBindViewHolder(holder: com.julioapps.commons.adapters.MyRecyclerViewAdapter.ViewHolder, position: Int) {
        val fileDirItem = fileDirItems[position]
        val view = holder.bindView(fileDirItem, false) { itemView, layoutPosition ->
            setupView(itemView, fileDirItem)
        }
        bindViewHolder(holder, position, view)
    }

    override fun getItemCount() = fileDirItems.size

    override fun prepareActionMode(menu: Menu) {}

    override fun actionItemPressed(id: Int) {}

    override fun getSelectableItemCount() = fileDirItems.size

    override fun onViewRecycled(holder: com.julioapps.commons.adapters.MyRecyclerViewAdapter.ViewHolder?) {
        super.onViewRecycled(holder)
        if (!activity.isActivityDestroyed()) {
            Glide.with(activity).clear(holder?.itemView?.list_item_icon!!)
        }
    }

    private fun setupView(view: View, fileDirItem: FileDirItem) {
        view.apply {
            list_item_name.text = fileDirItem.name
            list_item_name.setTextColor(textColor)
            list_item_details.setTextColor(textColor)

            if (fileDirItem.isDirectory) {
                list_item_icon.setImageDrawable(folderDrawable)
                list_item_details.text = getChildrenCnt(fileDirItem)
            } else {
                list_item_details.text = fileDirItem.size.formatSize()
                val path = fileDirItem.path
                val options = RequestOptions()
                        .centerCrop()
                        .error(fileDrawable)

                val itemToLoad = if (fileDirItem.name.endsWith(".apk", true)) {
                    val packageInfo = context.packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
                    if (packageInfo != null) {
                        val appInfo = packageInfo.applicationInfo
                        appInfo.sourceDir = path
                        appInfo.publicSourceDir = path
                        appInfo.loadIcon(context.packageManager)
                    } else {
                        path
                    }
                } else {
                    path
                }

                if (context != null) {
                    Glide.with(context).load(itemToLoad).transition(withCrossFade()).apply(options).into(list_item_icon)
                }
            }
        }
    }

    private fun getChildrenCnt(item: FileDirItem): String {
        val children = item.children
        return activity.resources.getQuantityString(R.plurals.items, children, children)
    }
}
