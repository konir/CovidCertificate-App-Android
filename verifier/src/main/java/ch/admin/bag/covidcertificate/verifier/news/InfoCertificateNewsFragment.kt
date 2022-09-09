package ch.admin.bag.covidcertificate.verifier.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import ch.admin.bag.covidcertificate.common.R
import ch.admin.bag.covidcertificate.common.extensions.getDrawableIdentifier
import ch.admin.bag.covidcertificate.verifier.databinding.DialogFragmentInfoCertificateNewsBinding
import ch.admin.bag.covidcertificate.verifier.databinding.ItemCertificateNewsBinding
import ch.admin.bag.covidcertificate.verifier.modes.ModesAndConfigViewModel

class InfoCertificateNewsFragment : DialogFragment() {

	companion object {

		fun newInstance(): InfoCertificateNewsFragment = InfoCertificateNewsFragment()
	}

	private var _binding: DialogFragmentInfoCertificateNewsBinding? = null
	private val binding get() = _binding!!

	private val viewModel by activityViewModels<ModesAndConfigViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setStyle(STYLE_NO_TITLE, R.style.CovidCertificate_ChooseModeDialog)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = DialogFragmentInfoCertificateNewsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		setupNews()

	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun setupNews() {
		val languageKey = getString(ch.admin.bag.covidcertificate.verifier.R.string.language_key)

		viewModel.configLiveData.observe(viewLifecycleOwner) { configModel ->
			val newsList = configModel.getInfoCovidCertificateNews(languageKey)
			binding.infoCertificateNewsTitle.text = newsList?.title
			binding.infoCertificateNewsList.removeAllViews()
			newsList?.newsItems?.forEach { covidCertificateNewsItem ->
				val itemView = ItemCertificateNewsBinding.inflate(layoutInflater, binding.infoCertificateNewsList, false)
				Log.d("mau", "setupNews: ${covidCertificateNewsItem.iconAndroid}")
				val iconIdentifier = requireContext().getDrawableIdentifier(covidCertificateNewsItem.iconAndroid ?: "")
				itemView.newsIcon.setImageResource(iconIdentifier)
				itemView.newsText.text = covidCertificateNewsItem.text
				binding.infoCertificateNewsList.addView(itemView.root)
			}
		}
		binding.infoCertificateNewsCloseButton.setOnClickListener {
			if (this@InfoCertificateNewsFragment.isVisible) dismiss()
		}
	}
}