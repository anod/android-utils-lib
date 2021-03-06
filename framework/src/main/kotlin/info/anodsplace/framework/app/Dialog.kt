package info.anodsplace.framework.app

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ArrayRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog

abstract class BaseDialog(val context: Context,
                          @StyleRes private val themeResId: Int,
                          @StringRes private val titleRes: Int) {

    fun create(): AlertDialog {
        val builder = AlertDialog.Builder(context, this.themeResId)
        if (this.titleRes > 0) {
            builder.setTitle(this.titleRes)
        }

        this.apply(builder)
        return builder.create()
    }

    abstract fun apply(builder: AlertDialog.Builder)

    fun show() { create().show() }
}


class DialogMessage(context: Context,
                    @StyleRes themeResId: Int,
                    @StringRes titleRes: Int,
                    @StringRes private val messageRes: Int,
                    private val message: CharSequence,
                    private val config: (builder: AlertDialog.Builder) -> Unit)
    : BaseDialog(context, themeResId, titleRes) {

    constructor(context: Context, themeResId: Int, titleRes: Int, messageRes: Int, config: (builder: AlertDialog.Builder) -> Unit)
            :this(context, themeResId, titleRes, messageRes, "", config)

    constructor(context: Context, themeResId: Int, titleRes: Int, message: CharSequence, config: (builder: AlertDialog.Builder) -> Unit)
            : this(context, themeResId, titleRes, 0, message, config)

    override fun apply(builder: AlertDialog.Builder) {
        if (this.messageRes > 0) {
            builder.setMessage(this.messageRes)
        } else {
            builder.setMessage(this.message)
        }
        this.config(builder)
    }
}

class DialogCustom(context: Context,
                   @StyleRes themeResId: Int,
                   @StringRes titleRes: Int,
                   @LayoutRes private val layoutRes: Int,
                   private val config: (view: View, builder: AlertDialog.Builder) -> Unit)
    : BaseDialog(context, themeResId, titleRes){

    override fun apply(builder: AlertDialog.Builder) {
        val view = LayoutInflater.from(context).inflate(this.layoutRes, null)
        builder.setView(view)
        this.config(view, builder)
    }
}

class DialogItems(context: Context,
                  @StyleRes themeResId: Int,
                  @StringRes titleRes: Int,
                  @ArrayRes private val itemsRes: Int,
                  val listener: (dialog: DialogInterface, which: Int) -> Unit)
    : BaseDialog(context, themeResId, titleRes){

    override fun apply(builder: AlertDialog.Builder) {
        builder.setItems(this.itemsRes, this.listener)
    }
}

class DialogSingleChoice(context: Context,
                         @StyleRes themeResId: Int,
                         @StringRes titleRes: Int,
                         @ArrayRes private val itemsRes: Int,
                         private val checkedItem: Int,
                         val listener: (dialog: DialogInterface, which: Int) -> Unit)
    : BaseDialog(context, themeResId, titleRes){

    constructor(context: Context, @StyleRes themeResId: Int, itemsRes: Int, checkedItem: Int, listener: (dialog: DialogInterface, which: Int) -> Unit)
            : this(context, themeResId, 0, itemsRes, checkedItem, listener)

    override fun apply(builder: AlertDialog.Builder) {
        builder.setSingleChoiceItems(this.itemsRes, this.checkedItem, this.listener)
    }
}