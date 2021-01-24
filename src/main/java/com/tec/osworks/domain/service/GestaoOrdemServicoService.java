package com.tec.osworks.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tec.osworks.api.model.Comentario;
import com.tec.osworks.domain.exception.EntidadeNaoEncontradaException;
import com.tec.osworks.domain.exception.NegocioException;
import com.tec.osworks.domain.model.Cliente;
import com.tec.osworks.domain.model.OrdemServico;
import com.tec.osworks.domain.model.StatusOrdemServico;
import com.tec.osworks.domain.repository.ClienteRepository;
import com.tec.osworks.domain.repository.ComentarioRepository;
import com.tec.osworks.domain.repository.OrdemServicoRepository;

@Service
public class GestaoOrdemServicoService {

	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ComentarioRepository comentarioRepository;
	
	public OrdemServico criar(OrdemServico ordemServico) {
		Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId()).orElseThrow(() -> new NegocioException("Cliente não encontrado"));
		
		ordemServico.setCliente(cliente);
		ordemServico.setStatus(StatusOrdemServico.ABERTA);
		ordemServico.setDataAbertura(OffsetDateTime.now());
		
		return ordemServicoRepository.save(ordemServico);
	}
	
	public void finalizar(Long ordemServicoId) {
		OrdemServico ordemServico = buscar(ordemServicoId);
		
		ordemServico.finalizar();
		
		ordemServicoRepository.save(ordemServico);
	}
	
	public void cancelar(Long ordemServicoId) {
		OrdemServico ordemServico = buscar(ordemServicoId);
		
		ordemServico.cancelar();
		
		ordemServicoRepository.save(ordemServico);
	}
	
	public Comentario adicionarComentario(Long OrdemServicoId, String descricao) {
		OrdemServico ordemServico = buscar(OrdemServicoId);
		
		Comentario comentario = new Comentario();
		comentario.setDataEnvio(OffsetDateTime.now());
		comentario.setDescricao(descricao);
		comentario.setOrdemServico(ordemServico);
		
		return comentarioRepository.save(comentario);
	}

	private OrdemServico buscar(Long OrdemServicoId) {
		return ordemServicoRepository.findById(OrdemServicoId).orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de Serviço não encontrada"));
	}
}
