package hpg.org.samplegithubrepobrowser.view.callback

interface BackPressedListener {
    /**
     * Action for the event of back button pressed.
     * Return true if the event consumed completely by this instance, false otherwise
     */
    fun onBackPressed(): Boolean
}