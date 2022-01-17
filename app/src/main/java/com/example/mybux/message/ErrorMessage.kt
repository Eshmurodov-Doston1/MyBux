package com.example.mybux.message

import android.content.Context
import com.example.domain.helpers.fm.ErrorItem
import  com.example.mybux.R


object ErrorMessage {
     fun getMessage(error: String, context: Context): ErrorItem {



         val errorMessage: ErrorItem = when (error) {
             "9006" -> ErrorItem(error, context.getString(R.string.no_check_fm))
             "9007" -> ErrorItem(error, context.getString(R.string.fm_count))
             "9008" -> ErrorItem(error, context.getString(R.string.check_no_info))
             "900D" -> ErrorItem(error, context.getString(R.string.back_fm))
             "9011" -> ErrorItem(error, context.getString(R.string.qqs_price))
             "9012" -> ErrorItem(error, context.getString(R.string.check_count))
             "9013" -> ErrorItem(error, context.getString(R.string.all_price))
             "9014" -> ErrorItem(error, context.getString(R.string.all_price_card))
             "9016" -> ErrorItem(error, context.getString(R.string.memory_full))
             "9018" -> ErrorItem(error, context.getString(R.string.date_time))
             "9019" -> ErrorItem(error, context.getString(R.string.save_inspaction))
             "9020" -> ErrorItem(error, context.getString(R.string.date_time_info))
             "9022" -> ErrorItem(error, context.getString(R.string.memory))
             "901C" -> ErrorItem(error, context.getString(R.string.send_ack))
             "901D" -> ErrorItem(error, context.getString(R.string.no_z))
             "902F" -> ErrorItem(error, context.getString(R.string.z_report_count_all))
             "9030" -> ErrorItem(error, context.getString(R.string.z_report_create))
             "9031" -> ErrorItem(error, context.getString(R.string.no_summ))
             "9032" -> ErrorItem(error, context.getString(R.string.no_return_card_summ))
             "9033" -> ErrorItem(error, context.getString(R.string.no_vat))
             "9034" -> ErrorItem(error, context.getString(R.string.create_z_fm_data))
             "9026" -> ErrorItem(error, context.getString(R.string.fm_my))
             "902D" -> ErrorItem(error, context.getString(R.string.no_create_Z))
             "902B" -> ErrorItem(error, context.getString(R.string.z_empty))
             "9021" -> ErrorItem(error, context.getString(R.string.z_clouse))
             "901E" -> ErrorItem(error, context.getString(R.string.get_ack))
             "9009" -> ErrorItem(error, context.getString(R.string.line_error))
             "9023" -> ErrorItem(error, context.getString(R.string.error_date))
             else -> ErrorItem(error, context.getString(R.string.error_code) + ":" + error)
         }
        return errorMessage
    }
}