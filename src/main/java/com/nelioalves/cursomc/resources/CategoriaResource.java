package com.nelioalves.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.dto.CategoriaDTO;
import com.nelioalves.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	// Método para Buscar uma Nova Categoria
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		Categoria obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	
	// Método para Inserir uma Nova Categoria
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDto){
		Categoria obj = service.fromDto(objDto);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	//Método para Atualizar uma Categoria
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDto, @PathVariable Integer id){
		Categoria obj = service.fromDto(objDto);
		obj.setId(id); // <= Garante que a categoria que vai ser atualizada tenha esse Id
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
		
	}
	
	// Método para Deletar uma Categoria
	
		@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
		public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
			service.deleteById(id);
			return ResponseEntity.noContent().build();
			
		}
		
		// Método para Buscar TODAS Categoria
		
		@RequestMapping(method=RequestMethod.GET)
		public ResponseEntity<List<CategoriaDTO>> findAll() {// Agora tenho um método que vai retornar uma lista de CategoriaDTO
			List<Categoria> list = service.findAll();// Busca a lista no banco
			List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList()); // E converto para DTO
			return ResponseEntity.ok().body(listDto);
		}
	
		
		// Método para Pegar uma Requisição e chamar o método de Serviço de forma ORDENADA
		
				@RequestMapping(value = "/page", method=RequestMethod.GET)
				public ResponseEntity<Page<CategoriaDTO>> findPage(
						@RequestParam(value = "page", defaultValue = "0") Integer page, 
						@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
						@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy, 
						@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
					Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction);
					Page<CategoriaDTO> listDto = list.map(obj -> new CategoriaDTO(obj)); 
					return ResponseEntity.ok().body(listDto);
				}
	
}