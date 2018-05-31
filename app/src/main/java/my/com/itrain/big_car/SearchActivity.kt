package my.com.itrain.big_car

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView

class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private var mQuery:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        val inflater = menuInflater
        inflater.inflate(R.menu.search_main_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val seacrhView = searchItem.actionView as SearchView
        setupSeachView(searchItem)

        if (mQuery != null){
            seacrhView.setQuery(mQuery, false)
        }

        return true
    }

    private fun setupSeachView(searchItem: MenuItem?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}
