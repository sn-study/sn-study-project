package sn.example.demo.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class PpurigiDtlcId implements Serializable {
	private Long id;
    private Integer seq;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}
