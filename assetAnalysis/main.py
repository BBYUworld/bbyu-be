from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from sqlalchemy import func
from pydantic import BaseModel
from config import SessionLocal, Base, engine

app = FastAPI()

# DB 세션을 가져오는 의존성
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# 모델 (ORM)
from sqlalchemy import Column, BigInteger, Enum, Boolean, DateTime

class Asset(Base):
    __tablename__ = 'asset'

    asset_id = Column(BigInteger, primary_key=True, index=True)
    user_id = Column(BigInteger)
    couple_id = Column(BigInteger)
    type = Column(Enum('DEPOSIT', 'STOCK', 'REAL_ESTATE', name="asset_type_enum"))
    amount = Column(BigInteger)
    created_at = Column(DateTime)
    updated_at = Column(DateTime)
    is_ended = Column(Boolean)

# 모든 테이블 생성
Base.metadata.create_all(bind=engine)

# 자산 카테고리 응답 모델
class AssetCategoryResponse(BaseModel):
    label: str
    amount: int
    percentage: float

# API 엔드포인트 - 현재 자산 조회
@app.get("/api/coupleAssets/{coupleId}", response_model=list[AssetCategoryResponse])
def get_couple_assets(coupleId: int, db: Session = Depends(get_db)):
    # 부부의 총 자산 계산
    total_assets = db.query(func.sum(Asset.amount)).filter(Asset.couple_id == coupleId).scalar() or 0

    if total_assets == 0:
        raise HTTPException(status_code=404, detail="해당 부부의 자산이 없습니다.")

    # 자산 카테고리별 계산
    cash_assets = db.query(func.sum(Asset.amount)).filter(Asset.couple_id == coupleId, Asset.type == 'DEPOSIT').scalar() or 0
    investment_assets = db.query(func.sum(Asset.amount)).filter(Asset.couple_id == coupleId, Asset.type == 'STOCK').scalar() or 0
    real_estate_assets = db.query(func.sum(Asset.amount)).filter(Asset.couple_id == coupleId, Asset.type == 'REAL_ESTATE').scalar() or 0

    # 응답 데이터 준비
    data = [
        AssetCategoryResponse(label="현금(예적금)", amount=cash_assets, percentage=round((cash_assets / total_assets) * 100, 2)),
        AssetCategoryResponse(label="투자(주식, 채권, 펀드)", amount=investment_assets, percentage=round((investment_assets / total_assets) * 100, 2)),
        AssetCategoryResponse(label="부동산, 토지", amount=real_estate_assets, percentage=round((real_estate_assets / total_assets) * 100, 2)),
    ]

    return data

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
