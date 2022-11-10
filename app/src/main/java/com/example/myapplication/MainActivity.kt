package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.OffsetEffect
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.text.isDigitsOnly
import com.example.myapplication.ui.theme.*
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {

    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = MainViewModel()

        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                LoginScreen(mainViewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun LoginScreen(viewModel: MainViewModel = MainViewModel()) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(listOf(lightBlue, blue))
            ), contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color.White,
            elevation = 20.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {

                Text(
                    text = "Create Account",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(20.dp),
                    color = Color.Black
                    , fontFamily = fontFamily,
                    fontWeight = FontWeight.ExtraBold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    editText(
                        modifier = Modifier.weight(1f),
                        text1 = "First Name",
                        viewModel = viewModel
                    )

                    editText(
                        modifier = Modifier.weight(1f),
                        text1 = "Last Name",
                        viewModel = viewModel
                    )
                }

                editText(text1 = "Email Id", viewModel = viewModel)
                editText(text1 = "Mobile No", viewModel = viewModel)
                editText(text1 = "Age", viewModel = viewModel)

                MyContent(modifier = Modifier, viewModel = viewModel)
                when (viewModel.selectedCourse.value) {
                    "Computer Science" -> {
                        viewModel.selectedSubCourse.value = "Data Science"
                        List_SubCat(
                            active_color = blue,
                            inActive_color = LightGray,
                            list = arrayListOf("Data Science", "AI"),
                            viewModel = viewModel
                        )
                    }

                    "Mathematics" -> {
                        viewModel.selectedSubCourse.value = "Calculus"
                        List_SubCat(
                            active_color = blue,
                            inActive_color = LightGray,
                            list = arrayListOf("Calculus", "Algebra"),
                            viewModel = viewModel
                        )

                    }
                    else -> {

                    }
                }

                Button(
                    onClick = {
                        Log.d("DATA!!!", "got data" + viewModel.firstName.value)
                        Log.d("DATA!!!", "got data" + viewModel.lastName.value)
                        Log.d("DATA!!!", "got data" + viewModel.email.value)
                        Log.d("DATA!!!", "got data" + viewModel.mobileNum.value)
                        Log.d("DATA!!!", "got data" + viewModel.selectedCourse.value)
                        Log.d("DATA!!!", "got data" + viewModel.selectedSubCourse.value)
                        Log.d("DATA!!!", "got data" + viewModel.age.value)


                        val fname = viewModel.firstName.value
                        val lname = viewModel.lastName.value
                        val email = viewModel.email.value
                        val mobile = viewModel.mobileNum.value
                        val sub_course = viewModel.selectedSubCourse.value
                        val course = viewModel.selectedCourse.value
                        val age = viewModel.age.value

                        if(fname.isNotEmpty() && lname.isNotEmpty() && email.isNotEmpty() && !course.equals("Choose Course"))
                        {
                            postDataToFirebase(user = User(fname,lname,email,mobile,age as Long?,course,sub_course))
                        }
                    },
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = bue),
                    modifier = Modifier
                        .align(
                            Alignment.CenterHorizontally
                        )
                        .padding(top = 10.dp, bottom = 5.dp)
                ) {
                    Text(text = "Sign Up", modifier = Modifier.padding(5.dp), fontSize = 16.sp)
                }

            }

        }

    }
}

@Composable
fun MyContent(modifier: Modifier, viewModel: MainViewModel) {

    // Declaring a boolean value to store
    // the expanded state of the Text Field
    var mExpanded by remember { mutableStateOf(false) }

    // Create a list of cities
    val mSubs = listOf("Computer Science", "Mathematics", "English")

    // Create a string value to store the selected city
    var mSelectedText by remember { mutableStateOf("Choose Course") }
    viewModel.selectedCourse.value = mSelectedText

    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(horizontal = 10.dp)) {

        // Create an Outlined Text Field
        // with icon and not expanded
//        OutlinedTextField(
//            value = mSelectedText,
//            onValueChange = {  },
//            modifier = Modifier
//                .fillMaxWidth()
//                .onGloballyPositioned { coordinates ->
//                    // This value is used to assign to
//                    // the DropDown the same width
//                    mTextFieldSize = coordinates.size.toSize()
//                }
//                .clickable { mExpanded = !mExpanded },
//            trailingIcon = {
//                Icon(icon,"contentDescription",
//                    Modifier.clickable { mExpanded = !mExpanded })
//            }
//        )
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .background(LightGray, shape = RoundedCornerShape(50.dp))
            .onGloballyPositioned { coordinates ->
                mTextFieldSize = coordinates.size.toSize()
            }
            .clickable {
                mExpanded = !mExpanded
            }, horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = mSelectedText, modifier = Modifier.padding(10.dp))
            Icon(
                modifier = Modifier.padding(10.dp),
                painter = rememberVectorPainter(image = icon),
                contentDescription = "icon"
            )

        }

        // Create a drop-down menu with list of cities,
        // when clicked, set the Text Field text as the city selected
        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
        ) {
            mSubs.forEach { label ->
                DropdownMenuItem(onClick = {
                    mSelectedText = label
                    mExpanded = false
                }) {
                    Text(text = label)
                }
            }
        }
    }
}

@Composable
fun editText(modifier: Modifier = Modifier, text1: String, viewModel: MainViewModel) {
    val focusManager = LocalFocusManager.current

    var text by remember {
        mutableStateOf("")
    }

    TextField(
        value = text,
        onValueChange = {
            text = it

            when (text1) {
                "First Name" -> {
                    if (text.isNotEmpty()) {
                        viewModel.firstName.value = text
                    }
                }
                "Last Name" -> {
                    if (text.isNotEmpty()) {
                        viewModel.lastName.value = text
                    }
                }

                "Email Id" -> {
                    if (text.contains("@gmail.com")) {
                        viewModel.email.value = text
                    }
                }

                "Mobile No" -> {
                    if (text.length == 10 && text.isDigitsOnly()) {
                        viewModel.mobileNum.value = text.toLong()
                    }
                }

                "Age" -> {
                    if (text.isDigitsOnly()) {
                        viewModel.age.value = text.toLong()
                    }
                }

            }


        },
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .then(modifier),
        shape = RoundedCornerShape(15.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            backgroundColor = LightGray
        ), label = {
            Text(text = text1, color = Color.Gray)
        }, keyboardActions = KeyboardActions(onNext = {
            focusManager.clearFocus(true)
        }), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )


}

@Composable
fun List_SubCat(
    active_color: Color,
    inActive_color: Color,
    list: ArrayList<String>,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {

    var selected_item by remember {
        mutableStateOf(0)
    }

    LazyRow(
        modifier = Modifier.padding(15.dp),
        horizontalArrangement = Arrangement.Start,
        content = {
            items(list.size) { it ->
                Box(modifier = Modifier
                    .padding(end = 5.dp)
                    .background(
                        if (selected_item == it) active_color else inActive_color,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 5.dp)
                    .clickable {
                        selected_item = it
                        viewModel.selectedSubCourse.value = list[selected_item]
                    }) {
                    Text(
                        text = list[it],
                        color = if (selected_item == it) Color.White else Color.Black,
                        modifier = Modifier.padding(10.dp),
                        fontSize = 16.sp
                    )
                }
            }
        })

}

fun postDataToFirebase(user: User){
    val firebaseDatabase = FirebaseDatabase.getInstance()

    firebaseDatabase.reference.child("Users").push().setValue(user).addOnSuccessListener {

    }.addOnFailureListener {

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        LoginScreen()
    }
}

data class User(
    val first_name: String? = null,
    val last_name: String? = null,
    val email: String? = null,
    val mobile: Long? = null,
    val age:Long? = null,
    val course: String? = null,
    val sub_course: String? = null
)