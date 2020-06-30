package com.example.demo.service;

import java.util.List;
import java.util.Objects;

import com.example.demo.entity.MstAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

import com.example.demo.repository.ClientRepository;
import com.example.demo.entity.Client;
import com.example.demo.searchform.ClientSearchForm;

@Service
@Transactional(rollbackOn = Exception.class)
public class ClientService {

  @Autowired
  private ClientRepository clientRepository;

  public Page<Client> getAll(Pageable pageable, ClientSearchForm searchForm) {
    Specification<Client> spec = Specification.where(idEqual(searchForm.getId() == null ? searchForm.getId() : searchForm.getId().replaceAll("　", "").replaceAll(" ", "")))
            .and(clientNameContains(searchForm.getClientName() == null ? searchForm.getClientName() : searchForm.getClientName().replaceAll("　", "").replaceAll(" ", "")))
            .and(clientNameKanaContains(searchForm.getClientNameKana() == null ? searchForm.getClientNameKana() : searchForm.getClientNameKana().replaceAll("　", "").replaceAll(" ", "")));
    return clientRepository.findAll(spec, pageable);
  }

  public List<Client> findAll() {
    return clientRepository.findAll();
  }

  public List<Client> search(Integer clientId, Integer cliantName, String cliantNameKana) {
    List<Client> result = clientRepository.findAll();
    return result;
  }

  public Client findOne(Long id) {
    return clientRepository.findById(id).orElse(null);
  }

  public Client save(Client client) {
    return clientRepository.save(client);
  }

  public void delete(Long id) {
    clientRepository.deleteById(id);
  }

  // 取引履歴機能の内容とページネーションを全検索
  public Page<Client> getAll(Pageable pageable) {
    return clientRepository.findAll(pageable);
  }

  /**
   *  ID検索
   */
  private static Specification<Client> idEqual(String id) {
    // ラムダ式で記述すると、引数のデータ型の指定が省略できる
    return id == "" || Objects.isNull(id) ? null : (root, query, cb) -> {
      return cb.equal(root.get("id"),  id);
    };
  }

  /**
   *  顧客名
   */
  private static Specification<Client> clientNameContains(String clientName) {
    // ラムダ式で記述すると、引数のデータ型の指定が省略できる
    return clientName == "" || Objects.isNull(clientName) ? null : (root, query, cb) -> {
      return cb.like(root.get("clientName"), "%" + clientName + "%");
    };
  }

  /**
   *  顧客名フリガナ検索
   */
  private static Specification<Client> clientNameKanaContains(String clientNameKana) {
    // ラムダ式で記述すると、引数のデータ型の指定が省略できる
    return clientNameKana == "" || Objects.isNull(clientNameKana) ? null : (root, query, cb) -> {
      return cb.like(root.get("clientNameKana"), "%" + clientNameKana + "%");
    };
  }
}
