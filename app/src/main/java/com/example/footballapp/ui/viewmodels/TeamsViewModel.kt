package com.example.footballapp.ui.viewmodels

import androidx.lifecycle.ViewModel

// TeamsViewModel este păstrat pentru compatibilitate cu navigarea existentă.
// Logica de afișare a echipelor a fost mutată în StandingsViewModel,
// care folosește /championships/view/ pentru a obține clasamentul complet.
class TeamsViewModel : ViewModel()