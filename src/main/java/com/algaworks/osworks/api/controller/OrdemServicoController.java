package com.algaworks.osworks.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.osworks.api.model.OrdemServicoInput;
import com.algaworks.osworks.api.model.OrdemServicoModel;
import com.algaworks.osworks.domain.model.OrdemServico;
import com.algaworks.osworks.domain.repository.OrdemServicoRepository;
import com.algaworks.osworks.domain.service.GestaoOrdemServicoService;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {
	
	@Autowired
	private GestaoOrdemServicoService service;
	
	@Autowired
	private OrdemServicoRepository repository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrdemServicoModel criar(@Valid @RequestBody OrdemServicoInput ordemServicoInput) {
		OrdemServico ordemServico = toEntity(ordemServicoInput);
 		return toModel(service.criar(ordemServico)); 
	}
	
	@GetMapping
	public List<OrdemServicoModel> listar() {
		return toCollectionModel(repository.findAll()) ;
	}
	
	@GetMapping("/{ordemServicoId}")
	public ResponseEntity<OrdemServicoModel> buscar(@PathVariable Long ordemServicoId) {
		 Optional<OrdemServico> ordemServico = repository.findById(ordemServicoId);
		 
		 if(ordemServico.isPresent()) {
			 OrdemServicoModel model = toModel(ordemServico.get());
			 return ResponseEntity.ok(model);
		 }
		 
		 return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{ordemServicoId}/finalizacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void finalizar(@PathVariable Long ordemServicoId) {
		service.finalizar(ordemServicoId);
	}
	
	@PutMapping("/{ordemServicoId}/cancelamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelar(@PathVariable Long ordemServicoId) {
		service.cancelar(ordemServicoId);
	}
	
	private OrdemServicoModel toModel(OrdemServico ordemServico) {
		return modelMapper.map(ordemServico, OrdemServicoModel.class);
	}
	
	private List<OrdemServicoModel> toCollectionModel(List<OrdemServico> ordensServicos) {
		return ordensServicos.stream()
				.map(ordemServico -> toModel(ordemServico))
				.collect(Collectors.toList());
	}
	
	private OrdemServico toEntity(OrdemServicoInput ordemServicoInput) {
		return modelMapper.map(ordemServicoInput, OrdemServico.class);
	}
}
