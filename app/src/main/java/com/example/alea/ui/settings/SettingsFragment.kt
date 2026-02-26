package com.example.alea.ui.settings

import android.content.Intent
import android.graphics.Color
import android.graphics.Insets
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alea.R
import com.example.alea.databinding.FragmentSettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure settings rows
        configureRow(binding.settingsEditProfile.root, R.drawable.ic_person, getString(R.string.settings_edit_profile))
        configureRow(binding.settingsPrivacy.root, R.drawable.ic_security, getString(R.string.settings_privacy))
        configureRow(binding.settingsNotificationsPref.root, R.drawable.ic_notifications, getString(R.string.settings_notifications))
        configureRow(binding.settingsTheme.root, R.drawable.ic_dark_mode, getString(R.string.settings_theme))
        configureRow(binding.settingsLanguage.root, R.drawable.ic_language, getString(R.string.settings_language))
        configureRow(binding.settingsHelp.root, R.drawable.ic_chat, getString(R.string.settings_help))
        configureRow(binding.settingsTerms.root, R.drawable.ic_description, getString(R.string.settings_terms))
        configureRow(binding.settingsVersion.root, R.drawable.ic_settings, "Versión 1.0.0")

        // Click listeners
        binding.settingsEditProfile.root.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_editProfile)
        }
        binding.settingsPrivacy.root.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_security)
        }

        // Notifications preferences
        binding.settingsNotificationsPref.root.setOnClickListener {
            showNotificationsDialog()
        }

        // Theme
        binding.settingsTheme.root.setOnClickListener {
            showThemeDialog()
        }

        // Language
        binding.settingsLanguage.root.setOnClickListener {
            showLanguageDialog()
        }

        // Help
        binding.settingsHelp.root.setOnClickListener {
            showHelpDialog()
        }

        // Terms
        binding.settingsTerms.root.setOnClickListener {
            showTermsDialog()
        }

        // Version
        binding.settingsVersion.root.setOnClickListener {
            Toast.makeText(requireContext(), "ALEA v1.0.0 · Build 2024.1\n© 2024 ALEA Team", Toast.LENGTH_LONG).show()
        }

        binding.settingsLogout.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Alea_Dialog)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                .setPositiveButton("Cerrar sesión") { _, _ ->
                    requireActivity().getSharedPreferences("alea_prefs", 0).edit()
                        .putBoolean("is_logged_in", false)
                        .putBoolean("is_first_launch", true)
                        .apply()
                    startActivity(Intent(requireContext(), com.example.alea.ui.onboarding.OnboardingActivity::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton("Cancelar", null)
                .create()
            dialog.show()
            styleDialogButtons(dialog)
        }
    }

    private fun showThemeDialog() {
        val themes = arrayOf("🌙  Oscuro (actual)", "☀️  Claro", "📱  Automático del sistema")
        var selected = 0
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Alea_Dialog)
            .setTitle("Tema de la app")
            .setSingleChoiceItems(themes, selected) { _, which -> selected = which }
            .setPositiveButton("Aplicar") { _, _ ->
                Toast.makeText(requireContext(), "Tema actualizado: ${themes[selected]}", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
        styleDialogButtons(dialog)
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("🇪🇸  Español (actual)", "🇬🇧  English", "🇫🇷  Français", "🇩🇪  Deutsch")
        var selected = 0
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Alea_Dialog)
            .setTitle("Idioma")
            .setSingleChoiceItems(languages, selected) { _, which -> selected = which }
            .setPositiveButton("Aplicar") { _, _ ->
                Toast.makeText(requireContext(), "Idioma: ${languages[selected]}", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
        styleDialogButtons(dialog)
    }

    private fun showNotificationsDialog() {
        val options = arrayOf("Retos recibidos", "Mensajes de amigos", "Resultados de retos", "Ranking semanal", "Logros desbloqueados")
        val checked = booleanArrayOf(true, true, true, true, false)
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Alea_Dialog)
            .setTitle("Preferencias de notificación")
            .setMultiChoiceItems(options, checked) { _, which, isChecked ->
                checked[which] = isChecked
            }
            .setPositiveButton("Guardar") { _, _ ->
                Toast.makeText(requireContext(), "Preferencias guardadas ✓", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
        styleDialogButtons(dialog)
    }

    private fun showHelpDialog() {
        val ctx = requireContext()
        val container = ScrollView(ctx)
        val content = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 40, 60, 40)
        }

        val faqs = listOf(
            "¿Cómo creo un reto?" to "Pulsa el botón \"+\" del menú inferior para iniciar el asistente de creación de retos paso a paso.",
            "¿Cómo gano Alea Coins?" to "Ganas Alea Coins completando retos, ganando apuestas, subiendo en el ranking y desbloqueando logros.",
            "¿Puedo cancelar un reto?" to "Puedes cancelar un reto antes de que el rival lo acepte. Una vez aceptado, el reto es definitivo.",
            "¿Cómo añado amigos?" to "Ve a la pestaña Amigos y pulsa \"Añadir amigo\". Puedes buscar por nombre de usuario.",
            "¿Qué es el reto semanal?" to "Cada semana hay un reto especial con premios exclusivos. ¡Participa desde la pantalla principal!"
        )

        faqs.forEach { (question, answer) ->
            content.addView(TextView(ctx).apply {
                text = "❓ $question"
                setTextColor(resources.getColor(R.color.white, null))
                textSize = 15f
                typeface = resources.getFont(R.font.poppins_semibold)
                setPadding(0, 16, 0, 4)
            })
            content.addView(TextView(ctx).apply {
                text = answer
                setTextColor(resources.getColor(R.color.color_text_secondary, null))
                textSize = 13f
                typeface = resources.getFont(R.font.poppins)
                setPadding(0, 0, 0, 16)
                setLineSpacing(4f, 1f)
            })
        }

        container.addView(content)
        val dialog = MaterialAlertDialogBuilder(ctx, R.style.Theme_Alea_Dialog)
            .setTitle("Ayuda y FAQ")
            .setView(container)
            .setPositiveButton("Cerrar", null)
            .create()
        dialog.show()
        styleDialogButtons(dialog)
    }

    private fun showTermsDialog() {
        val ctx = requireContext()
        val container = ScrollView(ctx)
        val content = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 40, 60, 40)
        }
        content.addView(TextView(ctx).apply {
            text = "Términos de Uso — ALEA\n\n" +
                "1. Aceptación de términos\n" +
                "Al utilizar ALEA aceptas los presentes términos y condiciones de uso.\n\n" +
                "2. Descripción del servicio\n" +
                "ALEA es una plataforma de retos sociales gamificados entre amigos. Las Alea Coins son una moneda virtual sin valor monetario real.\n\n" +
                "3. Conducta del usuario\n" +
                "Los usuarios se comprometen a mantener una conducta respetuosa. Está prohibido crear retos que promuevan actividades peligrosas o ilegales.\n\n" +
                "4. Privacidad\n" +
                "Tus datos personales son tratados según nuestra Política de Privacidad. No compartimos tu información con terceros sin tu consentimiento.\n\n" +
                "5. Propiedad intelectual\n" +
                "Todo el contenido de ALEA (diseños, logos, textos) es propiedad de ALEA Team.\n\n" +
                "6. Modificaciones\n" +
                "Nos reservamos el derecho de modificar estos términos en cualquier momento.\n\n" +
                "Última actualización: Enero 2024"
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 13f
            typeface = resources.getFont(R.font.poppins)
            setLineSpacing(4f, 1f)
        })

        container.addView(content)
        val dialog = MaterialAlertDialogBuilder(ctx, R.style.Theme_Alea_Dialog)
            .setTitle("Términos y condiciones")
            .setView(container)
            .setPositiveButton("Cerrar", null)
            .create()
        dialog.show()
        styleDialogButtons(dialog)
    }

    private fun styleDialogButtons(dialog: androidx.appcompat.app.AlertDialog) {
        // Force orange color on positive button and proper color on negative
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)?.apply {
            setTextColor(resources.getColor(R.color.color_primary_start, null))
            typeface = resources.getFont(R.font.poppins_semibold)
            textSize = 14f
            isAllCaps = false
        }
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)?.apply {
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            typeface = resources.getFont(R.font.poppins_medium)
            textSize = 14f
            isAllCaps = false
        }
        // Add horizontal margin so dialog doesn't touch screen edges
        dialog.window?.apply {
            setBackgroundDrawableResource(R.drawable.shape_dialog_bg)
            val params = attributes
            params.width = (resources.displayMetrics.widthPixels * 0.88).toInt()
            attributes = params
        }
    }

    private fun configureRow(row: View, iconRes: Int, title: String) {
        row.findViewById<ImageView>(R.id.settings_row_icon).setImageResource(iconRes)
        row.findViewById<TextView>(R.id.settings_row_title).text = title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
