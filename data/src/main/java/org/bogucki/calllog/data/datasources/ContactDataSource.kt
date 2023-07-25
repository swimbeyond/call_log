package org.bogucki.calllog.data.datasources

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import javax.inject.Inject

class ContactDataSource
    @Inject constructor (private val resolver: ContentResolver) {

    fun getContactNameByPhoneNumber(phoneNumber: String): String? {
        val projection = arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME
        )

        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )
        val cursor =
            resolver.query(uri, projection, null, null, null)

        cursor?.let {
            val nameColumn: Int = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            if (it.moveToFirst()) {
                val name = cursor.getString(nameColumn)
                cursor.close()
                return name
            }
        }
        return null
    }
}