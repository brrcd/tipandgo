package com.tip_n_go.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tip_n_go.R
import com.tip_n_go.data.incoming.Balance
import com.tip_n_go.data.incoming.BankCard
import com.tip_n_go.data.incoming.UnitResponseResult
import com.tip_n_go.databinding.FragmentDialogPayoutUserBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.*
import com.tip_n_go.ui.listener.DialogCloseButtonInterface
import com.tip_n_go.ui.listener.DialogDismissListener
import com.tip_n_go.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PayoutUserDialogFragment(private val listener: DialogDismissListener) :
    BottomSheetDialogFragment(), DialogCloseButtonInterface {

    private var _binding: FragmentDialogPayoutUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModel()
    private var bankCards = listOf<BankCard>()
    private var balance = Balance()
    private var selectedBankCard: BankCard? = null

    override fun getTheme() = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDialogPayoutUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        viewModel.balanceLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.bankCardsLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.payoutSuccessLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getBalance()
        viewModel.getUserBankCards()
    }

    private fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                if (data is Balance) {
                    setupBalance(data)
                }
                data.listCastCheck<BankCard> { setupBankCards(it) }
                if (data is UnitResponseResult) {
                    if (data.payoutSuccess) {
                        binding.withdrawButton.showSnack(getString(R.string.operation_successful))
                    }
                }
            }
            is UiState.Error -> {
                val error = uiState.exception.error
                binding.root.showSnack(error)
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupView() {
        setupCloseButton()
        binding.withdrawButton.setOnClickListener {
            if (selectedBankCard == null) {
                binding.root.showSnack(getString(R.string.select_bank_card))
                return@setOnClickListener
            }
            val amount = if (binding.amount.getText.isNotEmpty()) {
                binding.amount.getText.toDouble()
            } else {
                0.0
            }
            if (balance.balance != null) {
                if (amount > balance.balance!!) {
                    binding.root.showSnack(getString(R.string.insufficient_funds))
                    return@setOnClickListener
                } else if (amount == 0.0) {
                    binding.root.showSnack(getString(R.string.select_amount_to_withdraw))
                    return@setOnClickListener
                }
            }
            val hash = selectedBankCard?.hash
            viewModel.payoutUser(amount, hash!!)
        }
    }

    private fun setupBalance(balance: Balance) {
        this.balance = balance
        binding.balanceCard.balanceTotal.text = balance.balanceLocalized
        binding.balanceCard.actionButton.gone()
    }

    private fun setupBankCards(bankCards: List<BankCard>) {
        this.bankCards = bankCards
        val mList = bankCards.map { it.cardNumberMasked }.toMutableList()
        val adapter = createHintAdapter(mList)
        binding.bankCardSelector.adapter = adapter
        binding.bankCardSelector.setSelection(adapter.count)
        setOnBankCardSelected(bankCards)
    }

    private fun setOnBankCardSelected(bankCards: List<BankCard>) {
        binding.bankCardSelector.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, v: View?, index: Int, p3: Long) {
                if (index < bankCards.size) {
                    binding.bankCardHint.gone()
                    selectedBankCard = bankCards[index]
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun setupCloseButton() {
        binding.closeButton.setOnClickListener {
            this.dismiss()
            listener.onDismiss()
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        listener.onDismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
