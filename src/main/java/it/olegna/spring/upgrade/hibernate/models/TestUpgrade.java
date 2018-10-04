package it.olegna.spring.upgrade.hibernate.models;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Cache(region = "it.olegna.spring.upgrade.hibernate.models.TestUpgrade", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "TEST_UPGRADE")
public class TestUpgrade extends AbstractModel {

	private static final long serialVersionUID = -2154137332931276374L;

	private String colonna;
	private String col;
	@Column(name = "COLONNA_UNO")
	public String getColonna() {
		return colonna;
	}
	public void setColonna(String colonna) {
		this.colonna = colonna;
	}
	@Column(name = "COLONNA_DUE")
	public String getCol() {
		return col;
	}
	public void setCol(String col) {
		this.col = col;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((col == null) ? 0 : col.hashCode());
		result = prime * result + ((colonna == null) ? 0 : colonna.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestUpgrade other = (TestUpgrade) obj;
		if (col == null) {
			if (other.col != null)
				return false;
		} else if (!col.equals(other.col))
			return false;
		if (colonna == null) {
			if (other.colonna != null)
				return false;
		} else if (!colonna.equals(other.colonna))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "TestUpgrade [colonna=" + colonna + ", col=" + col + "]";
	}
}