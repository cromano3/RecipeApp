package com.christopherromano.culinarycompanion.viewmodel

import android.app.Application
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christopherromano.culinarycompanion.data.RecipeAppDatabase
import com.christopherromano.culinarycompanion.data.repository.SettingsScreenFirebaseRepository
import com.christopherromano.culinarycompanion.data.repository.SettingsScreenRepository
import com.christopherromano.culinarycompanion.datamodel.UiAlertStateSettingsScreenDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsScreenViewModel(
    application: Application,
    private val settingsScreenFirebaseRepository: SettingsScreenFirebaseRepository
): ViewModel() {


    private val repository: SettingsScreenRepository

    var authState: LiveData<Int>

    val uiAlertState = MutableStateFlow(UiAlertStateSettingsScreenDataModel())


    init {
        val appDb = RecipeAppDatabase.getInstance(application)
        val settingsScreenDao = appDb.SettingsScreenDao()
        repository = SettingsScreenRepository(settingsScreenDao)

        authState = settingsScreenFirebaseRepository.authState

    }

    fun updateDisplayName(){
        viewModelScope.launch {
            val currentDisplayName = settingsScreenFirebaseRepository.getCurrentDisplayName()

            uiAlertState.update {
                it.copy(
                    showChangeDisplayNameAlert = true,
                    displayName = currentDisplayName
                )
            }

        }


    }

    fun cancelChangeDisplayNameAlert(){
        uiAlertState.update {
            it.copy(
                showChangeDisplayNameAlert = false
            )
        }

    }

    fun confirmDisplayNameChange(){

        val bannedWords = listOf<String>("anal", "anus","arse", "arse", "ass", "ass-fucker", "asses", "assfucker", "assfukka", "asshole", "assholes", "a_s_s", "b!tch", "b00bs", "b17ch", "b1tch", "balls", "ballsack", "bastard", "beastial", "beastiality","bestial", "bestiality", "bi+ch", "biatch", "bitch", "bitcher", "bitchers", "bitches", "bitchin", "bitching", "bloody", "blow job", "blowjob", "blowjobs", "boiolas", "bollock", "bollok", "boner", "boob", "boobs", "booobs", "boooobs", "booooobs", "booooooobs", "breasts", "buceta", "bugger", "bum", "bunny fucker", "butt", "butthole", "buttmuch", "buttplug", "c0ck", "c0cksucker", "carpet muncher", "cawk", "chink", "cipa", "cl1t", "clit", "clitoris", "clits", "cnut", "cock", "cock-sucker", "cockface", "cockhead", "cockmunch", "cockmuncher", "cocks", "cocksuck", "cocksucked", "cocksucker", "cocksucking", "cocksucks", "cocksuka", "cocksukka", "cok", "cokmuncher", "coksucka", "coon", "cox", "crap", "cum", "cummer", "cumming", "cums", "cumshot", "cunilingus", "cunillingus", "cunnilingus", "cunt", "cuntlick", "cuntlicker", "cuntlicking", "cunts", "cyalis", "cyberfuc", "cyberfuck", "cyberfucked", "cyberfucker", "cyberfuckers", "cyberfucking", "d1ck", "damn", "dick", "dickhead", "dildo", "dildos", "dink", "dinks", "dirsa", "dlck", "dog-fucker", "doggin", "dogging", "donkeyribber", "doosh", "duche", "dyke", "ejaculate", "ejaculated", "ejaculates", "ejaculating", "ejaculatings", "ejaculation", "ejakulate", "f u c k", "f u c k e r", "f4nny", "fag", "fagging", "faggitt", "faggot", "faggs", "fagot", "fagots", "fags", "fanny", "fannyflaps", "fannyfucker", "fanyy", "fatass", "fcuk", "fcuker", "fcuking", "feck", "fecker", "felching", "fellate", "fellatio", "fingerfuck", "fingerfucked", "fingerfucker", "fingerfuckers", "fingerfucking", "fingerfucks", "fistfuck", "fistfucked", "fistfucker", "fistfuckers", "fistfucking", "fistfuckings", "fistfucks", "flange", "fook", "fooker", "fuck", "fucka", "fucked", "fucker", "fuckers", "fuckhead", "fuckheads", "fuckin", "fucking", "fuckings", "fuckingshitmotherfucker", "fuckme", "fucks", "fuckwhit", "fuckwit", "fudge packer", "fudgepacker", "fuk", "fuker", "fukker", "fukkin", "fuks", "fukwhit", "fukwit", "fux", "fux0r", "f_u_c_k", "gangbang", "gangbanged", "gangbangs", "gaylord", "gaysex", "goatse", "God", "god-dam", "god-damned", "goddamn", "goddamned", "hardcoresex", "hell", "heshe", "hoar", "hoare", "hoer", "homo", "hore", "horniest", "horny", "hotsex", "jack-off", "jackoff", "jap", "jerk-off", "jism", "jew", "jizm", "jizz", "kawk", "knob", "knobead", "knobed", "knobend", "knobhead", "knobjocky", "knobjokey", "kock", "kondum", "kondums", "kum", "kummer", "kumming", "kums", "kunilingus", "l3i+ch", "l3itch", "labia", "lust", "lusting", "m0f0", "m0fo", "m45terbate", "ma5terb8", "ma5terbate", "masochist", "master-bate", "masterb8", "masterbat*", "masterbat3", "masterbate", "masterbation", "masterbations", "masturbate", "mo-fo", "mof0", "mofo", "mothafuck", "mothafucka", "mothafuckas", "mothafuckaz", "mothafucked", "mothafucker", "mothafuckers", "mothafuckin", "mothafucking", "mothafuckings", "mothafucks", "mother fucker", "motherfuck", "motherfucked", "motherfucker", "motherfuckers", "motherfuckin", "motherfucking", "motherfuckings", "motherfuckka", "motherfucks", "muff", "mutha", "muthafecker", "muthafuckker", "muther", "mutherfucker", "n1gga", "n1gger", "nazi", "nigg3r", "nigg4h", "nigga", "niggah", "niggas", "niggaz", "nigger", "niggers", "nob", "nob jokey", "nobhead", "nobjocky", "nobjokey", "numbnuts", "nutsack", "orgasim", "orgasims", "orgasm", "orgasms", "p0rn", "pawn", "pecker", "penis", "penisfucker", "phonesex", "phuck", "phuk", "phuked", "phuking", "phukked", "phukking", "phuks", "phuq", "pigfucker", "pimpis", "piss", "pissed", "pisser", "pissers", "pisses", "pissflaps", "pissin", "pissing", "pissoff", "poop", "porn", "porno", "pornography", "pornos", "prick", "pricks", "pron", "pube", "pusse", "pussi", "pussies", "pussy", "pussys", "rectum", "retard", "rimjaw", "rimming", "s hit", "s.o.b.", "sadist", "schlong", "screwing", "scroat", "scrote", "scrotum", "semen", "sex", "sh!+", "sh!t", "sh1t", "shag", "shagger", "shaggin", "shagging", "shemale", "shi+", "shit", "shitdick", "shite", "shited", "shitey", "shitfuck", "shitfull", "shithead", "shiting", "shitings", "shits", "shitted", "shitter", "shitters", "shitting", "shittings", "shitty", "skank", "slut", "sluts", "smegma", "smut", "snatch", "son-of-a-bitch", "spac", "spunk", "s_h_i_t", "t1tt1e5", "t1tties", "teets", "teez", "testical", "testicle", "tit", "titfuck", "tits", "titt", "tittie5", "tittiefucker", "titties", "tittyfuck", "tittywank", "titwank", "tosser", "turd", "tw4t", "twat", "twathead", "twatty", "v14gra", "v1gra", "vagina", "viagra", "vulva", "w00se", "wang", "wank", "wanker", "wanky", "whore")
        var isClean = true

        if(uiAlertState.value.inputText.text.matches(".*(?:\\s|@|www|http|.com|dotcom).*".toRegex())){
            isClean = false

        }

        if(isClean) {
            for (word in bannedWords) {
                if (uiAlertState.value.inputText.text.contains(word, true)) {
                    isClean = false
                    break
                }
            }
        }

        if(isClean) {
            settingsScreenFirebaseRepository.updateDisplayName(uiAlertState.value.inputText.text)

            uiAlertState.update {
                it.copy(
                    showChangeDisplayNameAlert = false
                )
            }
        }
    }

    fun updateInputText(textFieldValue: TextFieldValue) {
        uiAlertState.update {
            it.copy(
                inputText = textFieldValue
            )
        }
    }

    fun triggerDeleteAccountAlert(){
        uiAlertState.update {
            it.copy(
                showDeleteAccountAlert = true
            )
        }
    }

    fun cancelDeleteAccountAlert(){
        uiAlertState.update {
            it.copy(
                showDeleteAccountAlert = false
            )
        }
    }


    fun showLicenses(){
        uiAlertState.update {
            it.copy(
                showLicenses = true
            )
        }
    }

    fun closeLicenses(){
        uiAlertState.update {
            it.copy(
                showLicenses = false
            )
        }
    }


    fun confirmDeleteAccount(){
        viewModelScope.launch {

            val deleteResult = settingsScreenFirebaseRepository.deleteAccount()

            if(deleteResult == "Success"){
                withContext(Dispatchers.IO) { repository.setLocalUserAsNew() }

                uiAlertState.update {
                    it.copy(
                        showDeleteAccountAlert = false,
                        showAccountWasDeletedMessage = true
                    )
                }
            }
//            else if(deleteResult == "FirebaseAuthRecentLoginRequiredException"){
//
//
//                //show "you need to log in again first to authenticate your account before it can be deleted" message
//
//                uiAlertState.update {
//                    it.copy(
//                        showDeleteAccountAlert = false
//                    )
//                }
//            }
            else{
                println("PROBLEM MESSAGE IS: $deleteResult")
                uiAlertState.update {
                    it.copy(
                        showDeleteAccountAlert = false
                    )
                }
            }
        }
    }

    fun cancelAccountWasDeletedMessage(){
        uiAlertState.update {
            it.copy(
                showAccountWasDeletedMessage = false
            )
        }
    }
}