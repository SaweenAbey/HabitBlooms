package com.example.habitblooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class GoalFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_goal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupBottomNavigation(view)
    }
    
    private fun setupBottomNavigation(view: View) {
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.aftersigninFragment -> {
                    findNavController().navigate(R.id.aftersigninFragment)
                    true
                }
                R.id.moodFragment -> {
                    findNavController().navigate(R.id.moodFragment)
                    true
                }
                R.id.hydrationFragment -> {
                    findNavController().navigate(R.id.hydrationFragment)
                    true
                }
                R.id.habbitFragment -> {
                    findNavController().navigate(R.id.habbitFragment)
                    true
                }
                R.id.settingsFragment -> {
                    findNavController().navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }
        
        // Set the current item as selected
        bottomNav?.selectedItemId = R.id.goalFragment
    }
}

