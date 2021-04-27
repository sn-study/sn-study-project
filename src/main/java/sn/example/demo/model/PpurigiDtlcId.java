package sn.example.demo.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class PpurigiDtlcId implements Serializable {
	private Long id;
    private Integer seq;

    PpurigiDtlcId() {}

    public PpurigiDtlcId(Long id, Integer seq) {
    	this.id = id;
    	this.seq = seq;
	}

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
