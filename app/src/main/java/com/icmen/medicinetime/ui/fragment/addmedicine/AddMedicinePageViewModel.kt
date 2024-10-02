package com.icmen.medicinetime.ui.fragment.addmedicine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.icmen.medicinetime.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddMedicinePageViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _saveMedicineResult = MutableLiveData<Resource<Boolean>>()
    val saveMedicineResult: LiveData<Resource<Boolean>> = _saveMedicineResult

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun saveMedicineData(
        medicineName: String,
        dosage: String,
        frequency: String,
        startDate: String,
        medicineTime: String,
        notes: String,
        endDate: String
    ) {
        if (medicineName.isEmpty() || dosage.isEmpty() || endDate.isEmpty() || frequency.isEmpty() || startDate.isEmpty() || medicineTime.isEmpty()) {
            _saveMedicineResult.value = Resource.Error("Lütfen tüm alanları doldurun.")
            return
        }

        try {
            val start = dateFormat.parse(startDate)
            val end = dateFormat.parse(endDate)

            if (start == null || end == null) {
                _saveMedicineResult.value = Resource.Error("Geçersiz tarih formatı.")
                return
            }

            if (start.after(end)) {
                _saveMedicineResult.value = Resource.Error("Başlangıç tarihi, bitiş tarihinden büyük olamaz.")
                return
            }

            val userId = firebaseAuth.currentUser?.uid
            if (userId == null) {
                _saveMedicineResult.value = Resource.Error("Kullanıcı oturumu bulunamadı.")
                return
            }
            val medicineData = hashMapOf(
                "userId" to userId,
                "name" to medicineName,
                "dosage" to dosage,
                "frequency" to frequency,
                "startDate" to startDate,
                "endDate" to endDate,
                "medicineTime" to medicineTime,
                "notes" to notes,
                "isActive" to "1"
            )

            _saveMedicineResult.value = Resource.Loading()
            firestore.collection("medicines")
                .add(medicineData)
                .addOnSuccessListener {
                    _saveMedicineResult.value = Resource.Success(true)
                }
                .addOnFailureListener { e ->
                    _saveMedicineResult.value = Resource.Error("İlaç kaydedilemedi: ${e.message}")
                }

        } catch (e: Exception) {
            _saveMedicineResult.value = Resource.Error("Tarih hatası: ${e.message}")
        }
    }
}
