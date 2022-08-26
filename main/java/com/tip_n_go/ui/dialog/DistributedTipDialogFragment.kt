package com.tip_n_go.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tip_n_go.R
import com.tip_n_go.data.incoming.DistributedTip
import com.tip_n_go.databinding.FragmentDialogDistributedTipBinding
import com.tip_n_go.tools.DISTRIBUTED_TIP_TAG
import com.tip_n_go.ui.adapter.TipDestinationsAdapter
import com.tip_n_go.ui.listener.DialogCloseButtonInterface
import kotlin.properties.Delegates

class DistributedTipDialogFragment : BottomSheetDialogFragment(), DialogCloseButtonInterface {

    private var _binding: FragmentDialogDistributedTipBinding? = null
    private val binding get() = _binding!!
    private var distributedTip by Delegates.notNull<DistributedTip>()

    override fun getTheme() = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        distributedTip = arguments?.getParcelable(DISTRIBUTED_TIP_TAG) ?: DistributedTip()
        _binding = FragmentDialogDistributedTipBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupCloseButton()
    }

    private fun setupView() {
        val title = distributedTip.user?.fullName
        val totalAmount = distributedTip.amountLocalized
        val distributionDate = distributedTip.date
        val tipDestinationList = distributedTip.destinations ?: listOf()

        binding.title.text = title
        binding.tipAmount.text = totalAmount
        binding.distributionDate.text = distributionDate
        val adapter = TipDestinationsAdapter()
        binding.tipDestinationsRecycler.adapter = adapter
        adapter.updateList(tipDestinationList)
    }

    override fun setupCloseButton() {
        binding.closeButton.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
