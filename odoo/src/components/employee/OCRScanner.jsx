import React, { useState, useCallback } from 'react'
import { useDropzone } from 'react-dropzone'
import { scanReceipt } from '../../api/expenses'
import { ScanLine, Upload, Loader2 } from 'lucide-react'
import toast from 'react-hot-toast'

export default function OCRScanner() {
  const [scanning, setScanning] = useState(false)
  const [result, setResult] = useState(null)

  const onDrop = useCallback(async (files) => {
    if (!files.length) return
    setScanning(true)
    setResult(null)
    try {
      const res = await scanReceipt(files[0])
      setResult(res.data)
      toast.success('Receipt scanned successfully!')
    } catch {
      toast.error('OCR API unavailable — showing demo data.')
      setResult({
        vendor: 'Starbucks Coffee',
        amount: 1250.0,
        date: '2026-03-25',
        category: 'Food',
        confidence: 0.94,
      })
    } finally {
      setScanning(false)
    }
  }, [])

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: { 'image/*': ['.png', '.jpg', '.jpeg', '.webp'] },
    maxFiles: 1,
  })

  return (
    <div className="max-w-2xl">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-navy-700">OCR Receipt Scanner</h1>
        <p className="text-sm text-navy-300 mt-1">Upload a receipt and let AI extract the details</p>
      </div>

      <div
        {...getRootProps()}
        className={`card border-2 border-dashed cursor-pointer text-center py-16 transition-all duration-300 ${
          isDragActive ? 'border-primary-400 bg-primary-50/30' : 'border-surface-300 hover:border-primary-300 hover:bg-primary-50/10'
        }`}
      >
        <input {...getInputProps()} />
        {scanning ? (
          <div className="flex flex-col items-center gap-4">
            <Loader2 size={40} className="text-primary-500 animate-spin" />
            <p className="text-navy-600 font-medium">Scanning receipt…</p>
            <p className="text-navy-300 text-sm">AI is extracting data from your receipt</p>
          </div>
        ) : (
          <div className="flex flex-col items-center gap-4">
            <div className="w-16 h-16 rounded-3xl bg-primary-50 border border-primary-100 flex items-center justify-center">
              {isDragActive ? <Upload size={28} className="text-primary-500" /> : <ScanLine size={28} className="text-primary-500" />}
            </div>
            <div>
              <p className="text-navy-700 font-semibold text-base">
                {isDragActive ? 'Drop your receipt here' : 'Drag & drop a receipt image'}
              </p>
              <p className="text-navy-300 text-sm mt-1">or click to browse • PNG, JPG, WEBP</p>
            </div>
          </div>
        )}
      </div>

      {result && (
        <div className="card mt-6 animate-fade-up">
          <div className="flex items-center gap-3 mb-5">
            <div className="w-10 h-10 rounded-xl bg-emerald-50 border border-emerald-100 flex items-center justify-center">
              <span className="text-emerald-600 text-lg">✓</span>
            </div>
            <div>
              <h3 className="text-base font-bold text-navy-700">Scan Results</h3>
              <p className="text-xs text-navy-300">Data extracted from your receipt</p>
            </div>
          </div>

          <div className="grid sm:grid-cols-2 gap-4">
            {[
              { label: 'Vendor', value: result.vendor },
              { label: 'Amount', value: `₹${result.amount}` },
              { label: 'Date', value: result.date },
              { label: 'Category', value: result.category },
            ].map((f) => (
              <div key={f.label} className="bg-surface-100 rounded-xl border border-surface-200 p-4">
                <span className="text-xs text-navy-300 uppercase tracking-wider font-semibold">{f.label}</span>
                <p className="text-navy-700 font-semibold mt-1.5">{f.value || '—'}</p>
              </div>
            ))}
          </div>

          {result.confidence && (
            <div className="mt-5 flex items-center gap-3">
              <span className="text-xs text-navy-300 font-semibold shrink-0">Confidence</span>
              <div className="flex-1 h-2.5 bg-surface-100 rounded-full overflow-hidden border border-surface-200">
                <div
                  className="h-full bg-gradient-to-r from-primary-500 to-primary-400 rounded-full transition-all duration-700"
                  style={{ width: `${result.confidence * 100}%` }}
                />
              </div>
              <span className="text-xs text-navy-600 font-bold">{Math.round(result.confidence * 100)}%</span>
            </div>
          )}
        </div>
      )}
    </div>
  )
}
