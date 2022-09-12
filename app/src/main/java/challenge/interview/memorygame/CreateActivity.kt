package challenge.interview.memorygame

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import challenge.interview.memorygame.Models.BoardSize
import challenge.interview.memorygame.Utils.EXTRA_BOARD_SIZE
import challenge.interview.memorygame.Utils.isPermissionGranted
import challenge.interview.memorygame.Utils.requestPermission

class CreateActivity : AppCompatActivity() {

    companion object {
        private const val PICK_PHOTO_CODE = 655
        private const val TAG = "CreateActivity"
        private const val MIN_GAME_NAME_LENGTH = 3
        private const val MAX_GAME_NAME_LENGTH = 14
        private const val READ_EXTERNAL_PHOTO_CODE = 248
        private const val READ_PHOTOS_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private lateinit var rvImagePicker:RecyclerView
    private lateinit var etGame:EditText
    private lateinit var btnSave:Button
    private lateinit var adapter: ImagePickerAdapter


    private lateinit var boardSize: BoardSize
    private var numImagesRequired = -1
    private val chosenImgUris = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        rvImagePicker = findViewById(R.id.rvImagePicker)
        etGame = findViewById(R.id.etGameName)
        btnSave = findViewById(R.id.btnSave)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        boardSize = intent.getSerializableExtra(EXTRA_BOARD_SIZE) as BoardSize
        numImagesRequired = boardSize.getNumPairs()
        supportActionBar?.title = "Choose pics (0/$numImagesRequired)"

        btnSave.setOnClickListener{
            saveDataToFireBase()
        }
        etGame.filters= arrayOf(InputFilter.LengthFilter(MAX_GAME_NAME_LENGTH))
        etGame.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {            }

            override fun afterTextChanged(p0: Editable?) {
                btnSave.isEnabled = shouldEnableSaveButton()
            }

        })

        adapter = ImagePickerAdapter(this,chosenImgUris,boardSize, object: ImagePickerAdapter.ImageClickListener{
            override fun onPlaceHolderClicked() {
                if(isPermissionGranted(this@CreateActivity,READ_PHOTOS_PERMISSION)) {
                    launchIntentForPhotos()
                }else {
                    requestPermission(this@CreateActivity, READ_PHOTOS_PERMISSION,READ_EXTERNAL_PHOTO_CODE)
                }
            }

        })
        rvImagePicker.adapter = adapter
        rvImagePicker.setHasFixedSize(true)
        rvImagePicker.layoutManager = GridLayoutManager(this,boardSize.getWidth())

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == READ_EXTERNAL_PHOTO_CODE){

        if (grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            launchIntentForPhotos()
        }else {
            Toast.makeText(this,"In order to create custom game, you need to provide access to your photos",Toast.LENGTH_SHORT).show()
        }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != PICK_PHOTO_CODE || resultCode != Activity.RESULT_OK || data == null){
            Log.w(TAG,"Did not get data back from the launched activity, user likely canceled flow")
            return
        }
        val selectedUri = data.data
        val clipData = data.clipData
        if (clipData != null){
            Log.i(TAG,"clipData numImages ${clipData.itemCount}: $clipData")
            for (i in 0 until clipData.itemCount){
                val clipItem = clipData.getItemAt(i)
                if(chosenImgUris.size < numImagesRequired){
                    chosenImgUris.add(clipItem.uri)
                }
            }
        }else if(selectedUri != null){
            Log.i(TAG,"data: $selectedUri")
            chosenImgUris.add(selectedUri)
        }
        adapter.notifyDataSetChanged()
        supportActionBar?.title = "Choose pics (${chosenImgUris.size}/$numImagesRequired)"
        btnSave.isEnabled = shouldEnableSaveButton()
    }

    private fun shouldEnableSaveButton(): Boolean {
        if(chosenImgUris.size != numImagesRequired) {
            return false
        }
        if(etGame.text.isBlank() || etGame.text.length < MIN_GAME_NAME_LENGTH){
            return false
        }
        return true
    }

    private fun launchIntentForPhotos() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        startActivityForResult(Intent.createChooser(intent,"Choose pics"),PICK_PHOTO_CODE)
    }
    private fun saveDataToFireBase() {
        Log.i(TAG,"saveDataToFireBase")


    }
}
